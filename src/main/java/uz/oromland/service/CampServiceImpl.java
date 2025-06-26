package uz.oromland.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.*;
import uz.oromland.enums.DocumentStatus;
import uz.oromland.exception.CampNotFoundException;
import uz.oromland.exception.CityNotFoundException;
import uz.oromland.exception.UserNotFoundException;
import uz.oromland.mapper.AttachmentMapper;
import uz.oromland.mapper.CampMapper;
import uz.oromland.mapper.UserMapper;
import uz.oromland.payload.AttachmentDTO;
import uz.oromland.payload.BookingCampDTO;
import uz.oromland.payload.CampDTO;
import uz.oromland.payload.CampFilterDTO;
import uz.oromland.repository.BookingCampRepository;
import uz.oromland.repository.CampRepository;
import uz.oromland.repository.CityRepository;
import uz.oromland.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CampServiceImpl implements CampService {

    private final CampRepository campRepository;
    private final CampMapper campMapper;
    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;
    private final BookingCampRepository bookingCampRepository;
    private final EntityManager entityManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    @Override
    public List<CampDTO> getAllCamps() {

        List<Camp> campList = campRepository.findAll();

        return campList.stream().map(campMapper::toDto).toList();

    }

    @Override
    public List<CampDTO> getCampsByCity(Long cityId) {

        List<Camp> camps = campRepository.findAllByCity_Id(cityId).orElseThrow(() -> new CityNotFoundException("City not found: " + cityId));
        return camps.stream().map(campMapper::toDto).toList();

    }

    @Override
    public CampDTO campByCityAndCamp(Long cityId, Long campId) {

        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new CityNotFoundException("Camp not found: " + campId));

        if (camp.getCity() == null || !camp.getCity().getId().equals(cityId)) {
            throw new CityNotFoundException("Camp does not belong to the specified city: " + cityId);
        }

        return campMapper.toDto(camp);

    }

    @Override
    public Long saveBooking(Long cityId,
                            Long campId,
                            BookingCampDTO bookingDTO,
                            MultipartFile healthNoteFile,
                            MultipartFile birthCertificateFile,
                            MultipartFile photoFile,
                            MultipartFile parentPassportFile,
                            MultipartFile guardianPermissionFile,
                            MultipartFile privilegeDocument) {


        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new CityNotFoundException("Camp not found: " + campId));

        if (camp.getCity() == null || !camp.getCity().getId().equals(cityId)) {
            throw new CityNotFoundException("Camp does not belong to the specified city: " + cityId);
        }

        AttachmentDTO healthFile = attachmentService.upload(healthNoteFile);
        AttachmentDTO birthFile = attachmentService.upload(birthCertificateFile);
        AttachmentDTO photo = attachmentService.upload(photoFile);
        AttachmentDTO parentPassport = attachmentService.upload(parentPassportFile);

        AttachmentDTO guardianPermission = null;
        if (guardianPermissionFile != null && !guardianPermissionFile.isEmpty()) {
            guardianPermission = attachmentService.upload(guardianPermissionFile);
        }

        AttachmentDTO privilegeDoc = null;
        if (privilegeDocument != null && !privilegeDocument.isEmpty()) {
            privilegeDoc = attachmentService.upload(privilegeDocument);
        }

        Attachment healthFileEntity = attachmentMapper.toEntity(healthFile);
        Attachment birthFileEntity = attachmentMapper.toEntity(birthFile);
        Attachment photoEntity = attachmentMapper.toEntity(photo);
        Attachment parentPassportEntity = attachmentMapper.toEntity(parentPassport);
        Attachment guardianPermissionEntity = null;
        if (guardianPermission != null) {
            guardianPermissionEntity = attachmentMapper.toEntity(guardianPermission);
        }

        Attachment privilegeDocEntity = null;
        if (privilegeDoc != null) {
            privilegeDocEntity = attachmentMapper.toEntity(privilegeDoc);
        }

        BookingCamp bookingCamp = new BookingCamp(
                null, // ID will be generated by the database
                bookingDTO.getFirstName(),
                bookingDTO.getLastName(),
                bookingDTO.getBirthDate(),
                bookingDTO.getGender(),
                bookingDTO.getDocumentNumber(),
                bookingDTO.getAddress(),
                bookingDTO.getGuardianFirstName(),
                bookingDTO.getGuardianLastName(),
                bookingDTO.getGuardianPhone(),
                bookingDTO.getGuardianDocument(),
                bookingDTO.getGuardianJob(),
                healthFileEntity,
                birthFileEntity,
                photoEntity,
                parentPassportEntity,
                guardianPermissionEntity,
                privilegeDocEntity,
                DocumentStatus.PENDING
        );

        BookingCamp bookingCamp1 = bookingCampRepository.save(bookingCamp);


        return bookingCamp1.getId();


    }

    @Override
    public List<CampDTO> getFilter(CampFilterDTO filterDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Camp> criteriaQuery = criteriaBuilder.createQuery(Camp.class);
        Root<Camp> rootCamp = criteriaQuery.from(Camp.class);
        List<Predicate> predicates = new ArrayList<>();

        String search = filterDTO.getSearch();
        if (Objects.nonNull(search)) {
            String name = Camp.Fields.name;
            String description = Camp.Fields.description;
            Predicate predicateName = criteriaBuilder.like(rootCamp.get(name), "%" + search + "%");
            Predicate predicateDescription = criteriaBuilder.like(rootCamp.get(description), "%" + search + "%");
            Predicate predicate = criteriaBuilder.or(predicateName, predicateDescription);
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getName())) {
            String name = Camp.Fields.name;
            Predicate predicate = criteriaBuilder.equal(rootCamp.get(name), filterDTO.getName());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getDescription())) {
            String description = Camp.Fields.description;
            Predicate predicate = criteriaBuilder.like(rootCamp.get(description), "%" + filterDTO.getDescription() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getLocation())) {
            String location = Camp.Fields.location;
            Predicate predicate = criteriaBuilder.like(rootCamp.get(location), "%" + filterDTO.getLocation() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getAgeFrom())) {
            String ageFrom = Camp.Fields.ageFrom;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootCamp.get(ageFrom), filterDTO.getAgeFrom());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getAgeTo())) {
            String ageTo = Camp.Fields.ageTo;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootCamp.get(ageTo), filterDTO.getAgeTo());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinDuration())) {
            String duration = Camp.Fields.duration;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootCamp.get(duration), filterDTO.getMinDuration());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxDuration())) {
            String duration = Camp.Fields.duration;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootCamp.get(duration), filterDTO.getMaxDuration());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getAvailableSeasons())) {
            String availableSeasons = Camp.Fields.availableSeasons;
            Predicate predicate = criteriaBuilder.like(rootCamp.get(availableSeasons), "%" + filterDTO.getAvailableSeasons() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getContactPhone())) {
            String contactPhone = Camp.Fields.contactPhone;
            Predicate predicate = criteriaBuilder.like(rootCamp.get(contactPhone), "%" + filterDTO.getContactPhone() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getStartDateFrom())) {
            String startDate = Camp.Fields.startDate;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootCamp.get(startDate), filterDTO.getStartDateFrom());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getStartDateTo())) {
            String startDate = Camp.Fields.startDate;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootCamp.get(startDate), filterDTO.getStartDateTo());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getEndDateFrom())) {
            String endDate = Camp.Fields.endDate;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootCamp.get(endDate), filterDTO.getEndDateFrom());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getEndDateTo())) {
            String endDate = Camp.Fields.endDate;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootCamp.get(endDate), filterDTO.getEndDateTo());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinParticipants())) {
            String maxParticipants = Camp.Fields.maxParticipants;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootCamp.get(maxParticipants), filterDTO.getMinParticipants());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxParticipants())) {
            String maxParticipants = Camp.Fields.maxParticipants;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootCamp.get(maxParticipants), filterDTO.getMaxParticipants());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getIsActive())) {
            String isActive = Camp.Fields.isActive;
            Predicate predicate = criteriaBuilder.equal(rootCamp.get(isActive), filterDTO.getIsActive());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinPrice())) {
            String price = Camp.Fields.price;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootCamp.get(price), filterDTO.getMinPrice());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxPrice())) {
            String price = Camp.Fields.price;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootCamp.get(price), filterDTO.getMaxPrice());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getCityName())) {
            String cityName = City.Fields.name;
            Predicate predicate = criteriaBuilder.like(rootCamp.join("city").get(cityName), "%" + filterDTO.getCityName() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getManagerName())) {
            String username = User.Fields.username;
            String firstName = User.Fields.firstName;
            String lastName = User.Fields.lastName;

            Predicate predicateName = criteriaBuilder.like(rootCamp.get(username), "%" + search + "%");
            Predicate predicateFirstName = criteriaBuilder.like(rootCamp.get(firstName), "%" + filterDTO.getManagerName() + "%");
            Predicate predicateLastName = criteriaBuilder.like(rootCamp.get(lastName), "%" + filterDTO.getManagerName() + "%");

            Predicate predicate = criteriaBuilder.or(predicateName, predicateFirstName, predicateLastName);
            predicates.add(predicate);
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        List<Camp> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultList.stream().map(campMapper::toDto).toList();
    }

    @Override
    public CampDTO getCampById(Long id) {

        Camp camp = campRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException("Camp not found: " + id));
        return campMapper.toDto(camp);

    }

    @Override
    public CampDTO createCamp(CampDTO campDTO, List<MultipartFile> attachments) {
        // 1. Upload and map the new files (if any)
        List<Attachment> attachmentList = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile attachment : attachments) {
                AttachmentDTO uploadedDTO = attachmentService.upload(attachment); // Upload file
                attachmentList.add(attachmentMapper.toEntity(uploadedDTO));       // Map to entity
            }
        }

        // 2. Map existing AttachmentDTOs from the DTO (if any)
        if (campDTO.getAttachmentDTOS() != null && !campDTO.getAttachmentDTOS().isEmpty()) {
            List<Attachment> existingAttachments = campDTO.getAttachmentDTOS()
                    .stream()
                    .map(attachmentMapper::toEntity)
                    .toList();
            attachmentList.addAll(existingAttachments);
        }

        // 3. Resolve manager and city references
        User manager = userRepository.findById(campDTO.getManagerDTO().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + campDTO.getManagerDTO().getId()));
        City city = cityRepository.findById(campDTO.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found with id: " + campDTO.getCityId()));

        // 4. Build the Camp entity
        Camp camp = new Camp(
                null,
                campDTO.getName(),
                campDTO.getDescription(),
                campDTO.getLocation(),
                campDTO.getAgeFrom(),
                campDTO.getAgeTo(),
                campDTO.getNumberOfExchanges(),
                campDTO.getDuration(),
                campDTO.getAvailableSeasons(),
                campDTO.getContactPhone(),
                campDTO.getStartDate(),
                campDTO.getEndDate(),
                campDTO.getMaxParticipants(),
                campDTO.getCurrentParticipants(),
                campDTO.getIsActive(),
                attachmentList, // Use the combined list
                manager,
                campDTO.getPrice(),
                city
        );

        // 5. Save and return
        Camp savedCamp = campRepository.save(camp);
        return campMapper.toDto(savedCamp);
    }


    @Override
    public CampDTO updateCamp(Long id, CampDTO campDTO, List<MultipartFile> attachments) {

        // 1. Mavjud Camp ni topamiz
        Camp existingCamp = campRepository.findById(id)
                .orElseThrow(() -> new CampNotFoundException("Camp not found: " + id));

        // 2. Attachmentlarni to‘playmiz (yangi va eski)
        List<Attachment> updatedAttachments = new ArrayList<>();

        // 2.1. Fayl bo‘lsa, yuklab, qo‘shamiz
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile attachment : attachments) {
                AttachmentDTO uploadedDTO = attachmentService.upload(attachment);
                updatedAttachments.add(attachmentMapper.toEntity(uploadedDTO));
            }
        }

        // 2.2. DTOdan kelgan attachmentlar bo‘lsa, ularni qo‘shamiz
        if (campDTO.getAttachmentDTOS() != null && !campDTO.getAttachmentDTOS().isEmpty()) {
            List<Attachment> attachmentEntities = campDTO.getAttachmentDTOS()
                    .stream()
                    .map(attachmentMapper::toEntity)
                    .toList();
            updatedAttachments.addAll(attachmentEntities);
        }

        // 3. Barcha maydonlarni yangilash (faqat mos keladiganlarini)
        existingCamp.setName(campDTO.getName());
        existingCamp.setDescription(campDTO.getDescription());
        existingCamp.setLocation(campDTO.getLocation());
        existingCamp.setAgeFrom(campDTO.getAgeFrom());
        existingCamp.setAgeTo(campDTO.getAgeTo());
        existingCamp.setNumberOfExchanges(campDTO.getNumberOfExchanges());
        existingCamp.setDuration(campDTO.getDuration());
        existingCamp.setAvailableSeasons(campDTO.getAvailableSeasons());
        existingCamp.setContactPhone(campDTO.getContactPhone());
        existingCamp.setStartDate(campDTO.getStartDate());
        existingCamp.setEndDate(campDTO.getEndDate());
        existingCamp.setMaxParticipants(campDTO.getMaxParticipants());
        existingCamp.setCurrentParticipants(campDTO.getCurrentParticipants());
        existingCamp.setIsActive(campDTO.getIsActive());
        existingCamp.setPrice(campDTO.getPrice());

        // 4. Attachmentlarni to‘liq yangilash (agar kerak bo‘lsa)
        if (!updatedAttachments.isEmpty()) {
            existingCamp.setAttachment(updatedAttachments);
        }

        // 5. Manager va City ham yangilanishi kerak bo‘lsa, topib, yangilaymiz
        if (campDTO.getManagerDTO() != null && campDTO.getManagerDTO().getId() != null) {
            User manager = userRepository.findById(campDTO.getManagerDTO().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found: " + campDTO.getManagerDTO().getId()));
            existingCamp.setManager(manager);
        }

        if (campDTO.getCityId() != null) {
            City city = cityRepository.findById(campDTO.getCityId())
                    .orElseThrow(() -> new CityNotFoundException("City not found: " + campDTO.getCityId()));
            existingCamp.setCity(city);
        }

        // 6. Saqlaymiz va DTOga map qilamiz
        Camp savedCamp = campRepository.save(existingCamp);
        return campMapper.toDto(savedCamp);
    }

    @Override
    public void deleteCampById(Long id) {
        // 1. Campni topamiz
        Camp camp = campRepository.findById(id)
                .orElseThrow(() -> new CampNotFoundException("Camp not found: " + id));

        // 2. Attachmentlarni to‘plab, har birini o‘chiramiz
        if (camp.getAttachment() != null && !camp.getAttachment().isEmpty()) {
            for (Attachment attachment : camp.getAttachment()) {
                attachmentService.delete(attachment.getId());
            }
        }

        // 3. Campni o‘chiramiz
        campRepository.deleteById(id);
    }


}
