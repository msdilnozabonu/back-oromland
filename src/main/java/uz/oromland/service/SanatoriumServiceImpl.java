package uz.oromland.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.oromland.entity.Attachment;
import uz.oromland.entity.BookingSanatorium;
import uz.oromland.entity.City;
import uz.oromland.entity.Sanatorium;
import uz.oromland.entity.User;
import uz.oromland.enums.DocumentStatus;
import uz.oromland.exception.SanatoriumNotFountException;
import uz.oromland.exception.UserNotFoundException;
import uz.oromland.mapper.AttachmentMapper;
import uz.oromland.mapper.UserMapper;
import uz.oromland.payload.*;

import uz.oromland.mapper.SanatoriumMapper;
import uz.oromland.repository.BookingSanatoriumRepository;
import uz.oromland.repository.CityRepository;
import uz.oromland.repository.SanatoriumRepository;
import uz.oromland.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SanatoriumServiceImpl implements SanatoriumService {

    private final SanatoriumRepository sanatoriumRepository;
    private final SanatoriumMapper sanatoriumMapper;
    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final BookingSanatoriumRepository bookingSanatoriumRepository;
    private final EntityManager entityManager;
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @Override
    public List<SanatoriumDTO> getAllSanatoriumByCityId(Long cityId) {
        List<uz.oromland.entity.Sanatorium> cityAll = sanatoriumRepository.findAllByCity_Id(cityId);
        return cityAll.stream().map(sanatoriumMapper::toDTO).toList();
    }

    @Override
    public SanatoriumDTO getSanatoriumByIdAndCityId(Long cityId, Long sanatoriumId) {

        Sanatorium sanatorium = sanatoriumRepository.findById(sanatoriumId)
                .orElseThrow(() -> new RuntimeException("Sanatorium not found"));

        if (!sanatorium.getCity().getId().equals(cityId)) {
            throw new RuntimeException("Sanatorium does not belong to the specified city");
        }

        return sanatoriumMapper.toDTO(sanatorium);

    }

    @Override
    public Long saveBooking(Long cityId,
                            Long sanatoriumId,
                            BookingSanatoriumDTO bookingSanatoriumDTO,
                            MultipartFile passportCopyFile,
                            MultipartFile healthNoteFile,
                            MultipartFile vaccinationFile,
                            MultipartFile photoFile,
                            MultipartFile givenDocumentByWorkplace) {

        Sanatorium sanatorium = sanatoriumRepository.findById(sanatoriumId).orElseThrow(() -> new RuntimeException("Sanatorium not found"));
        if (!sanatorium.getCity().getId().equals(cityId)) {
            throw new RuntimeException("Sanatorium does not belong to the specified city");
        }

        Attachment passportEntity = attachmentMapper.toEntity(attachmentService.upload(passportCopyFile));
        Attachment healthNoteEntity = attachmentMapper.toEntity(attachmentService.upload(healthNoteFile));
        Attachment vaccinationEntity = attachmentMapper.toEntity(attachmentService.upload(vaccinationFile));
        Attachment photoEntity = null;
        if (photoFile != null) {
            photoEntity = attachmentMapper.toEntity(attachmentService.upload(photoFile));
        }
        Attachment givenDocumentEntity = null;
        if (givenDocumentByWorkplace != null) {
            givenDocumentEntity = attachmentMapper.toEntity(attachmentService.upload(givenDocumentByWorkplace));
        }

        UserDTO userDTO = userService.getUserById(bookingSanatoriumDTO.getUser().getId());

        User user = userMapper.toEntity(userDTO);

        BookingSanatorium bookingSanatorium = new BookingSanatorium(
                null,
                user,
                sanatorium,
                bookingSanatoriumDTO.getStartDate(),
                bookingSanatoriumDTO.getEndDate(),
                bookingSanatoriumDTO.getDurationDays(),
                bookingSanatoriumDTO.getTotalPrice(),
                DocumentStatus.PENDING,
                passportEntity,
                healthNoteEntity,
                vaccinationEntity,
                photoEntity,
                givenDocumentEntity
        );

        BookingSanatorium bookingSanatorium1 = bookingSanatoriumRepository.save(bookingSanatorium);
        return bookingSanatorium1.getId();


    }

    @Override
    public List<SanatoriumDTO> getFilter(SanatoriumFilterDTO filterDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Sanatorium> criteriaQuery = criteriaBuilder.createQuery(Sanatorium.class);
        Root<Sanatorium> rootSanatorium = criteriaQuery.from(Sanatorium.class);
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(filterDTO.getSearch())) {
            String name = Sanatorium.Fields.name;
            String description = Sanatorium.Fields.description;
            Predicate predicateName = criteriaBuilder.like(rootSanatorium.get(name), "%" + filterDTO.getSearch() + "%");
            Predicate predicateDescription = criteriaBuilder.like(rootSanatorium.get(description), "%" + filterDTO.getSearch() + "%");
            Predicate predicate = criteriaBuilder.or(predicateName, predicateDescription);
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getName())) {
            String name = Sanatorium.Fields.name;
            Predicate predicate = criteriaBuilder.equal(rootSanatorium.get(name), filterDTO.getName());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getDescription())) {
            String description = Sanatorium.Fields.description;
            Predicate predicate = criteriaBuilder.like(rootSanatorium.get(description), "%" + filterDTO.getDescription() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getAvailableSeasons())) {
            String availableSeasons = Sanatorium.Fields.availableSeasons;
            Predicate predicate = criteriaBuilder.like(rootSanatorium.get(availableSeasons), "%" + filterDTO.getAvailableSeasons() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinTotalCapacity())) {
            String totalCapacity = Sanatorium.Fields.totalCapacity;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootSanatorium.get(totalCapacity), filterDTO.getMinTotalCapacity());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxTotalCapacity())) {
            String totalCapacity = Sanatorium.Fields.totalCapacity;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootSanatorium.get(totalCapacity), filterDTO.getMaxTotalCapacity());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinCurrentCapacity())) {
            String currentCapacity = Sanatorium.Fields.currentCapacity;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootSanatorium.get(currentCapacity), filterDTO.getMinCurrentCapacity());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxCurrentCapacity())) {
            String currentCapacity = Sanatorium.Fields.currentCapacity;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootSanatorium.get(currentCapacity), filterDTO.getMaxCurrentCapacity());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getIsActive())) {
            String isActive = Sanatorium.Fields.isActive;
            Predicate predicate = criteriaBuilder.equal(rootSanatorium.get(isActive), filterDTO.getIsActive());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getPhoneNumber())) {
            String phoneNumber = Sanatorium.Fields.phoneNumber;
            Predicate predicate = criteriaBuilder.like(rootSanatorium.get(phoneNumber), "%" + filterDTO.getPhoneNumber() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getLocation())) {
            String location = Sanatorium.Fields.location;
            Predicate predicate = criteriaBuilder.like(rootSanatorium.get(location), "%" + filterDTO.getLocation() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinOneDayPrice())) {
            String oneDayPrice = Sanatorium.Fields.oneDayPrice;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootSanatorium.get(oneDayPrice), filterDTO.getMinOneDayPrice());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxOneDayPrice())) {
            String oneDayPrice = Sanatorium.Fields.oneDayPrice;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootSanatorium.get(oneDayPrice), filterDTO.getMaxOneDayPrice());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMinTotalPrice())) {
            String totalPrice = Sanatorium.Fields.totalPrice;
            Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(rootSanatorium.get(totalPrice), filterDTO.getMinTotalPrice());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getMaxTotalPrice())) {
            String totalPrice = Sanatorium.Fields.totalPrice;
            Predicate predicate = criteriaBuilder.lessThanOrEqualTo(rootSanatorium.get(totalPrice), filterDTO.getMaxTotalPrice());
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getCityName())) {
            String cityName = City.Fields.name;
            Predicate predicate = criteriaBuilder.like(rootSanatorium.join("city").get(cityName), "%" + filterDTO.getCityName() + "%");
            predicates.add(predicate);
        }

        if (Objects.nonNull(filterDTO.getManagerName())) {
            String username = User.Fields.username;
            String firstName = User.Fields.firstName;
            String lastName = User.Fields.lastName;

            Predicate predicateName = criteriaBuilder.like(rootSanatorium.get(username), "%" + filterDTO.getSearch() + "%");
            Predicate predicateFirstName = criteriaBuilder.like(rootSanatorium.get(firstName), "%" + filterDTO.getSearch() + "%");
            Predicate predicateLastName = criteriaBuilder.like(rootSanatorium.get(lastName), "%" + filterDTO.getSearch() + "%");
            Predicate predicate = criteriaBuilder.or(predicateName, predicateFirstName, predicateLastName);


            predicates.add(predicate);
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        List<Sanatorium> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultList.stream().map(sanatoriumMapper::toDTO).toList();
    }


    @Override
    public List<SanatoriumDTO> getAllSanatoriums() {

        List<Sanatorium> allSanatoriums = sanatoriumRepository.findAll();
        return allSanatoriums.stream().map(sanatoriumMapper::toDTO).toList();

    }

    @Override
    public SanatoriumDTO getSanatoriumById(Long id) {

        Sanatorium sanatorium = sanatoriumRepository.findById(id)
                .orElseThrow(() -> new SanatoriumNotFountException("Sanatorium not found", HttpStatus.NOT_FOUND));
        return sanatoriumMapper.toDTO(sanatorium);
    }

    @Override
    public SanatoriumDTO createSanatorium(SanatoriumDTO sanatoriumDTO, List<MultipartFile> attachments) {

        List<Attachment> attachmentEntities = new ArrayList<>();
        for (MultipartFile attachment : attachments) {

            AttachmentDTO attachmentDTO = attachmentService.upload(attachment);

            Attachment attachmentEntity = attachmentMapper.toEntity(attachmentDTO);

            attachmentEntities.add(attachmentEntity);
        }

        City city = cityRepository.findById(sanatoriumDTO.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));


        List<User> managers = new ArrayList<>();
        sanatoriumDTO.getManager().forEach(manager -> {
                    User user = userRepository.findById(manager.getId())
                            .orElseThrow(() -> new UserNotFoundException("Manager not found"));
                    managers.add(user);
                }
        );


        Sanatorium sanatorium = new Sanatorium(
                null,
                sanatoriumDTO.getName(),
                sanatoriumDTO.getDescription(),
                sanatoriumDTO.getAvailableSeasons(),
                sanatoriumDTO.getCreatedBy(),
                sanatoriumDTO.getTotalCapacity(),
                sanatoriumDTO.getCurrentCapacity(),
                sanatoriumDTO.getIsActive(),
                sanatoriumDTO.getPhoneNumber(),
                sanatoriumDTO.getLocation(),
                sanatoriumDTO.getOneDayPrice(),
                sanatoriumDTO.getTotalPrice(),
                city,
                managers,
                attachmentEntities
        );
        Sanatorium savedSanatorium = sanatoriumRepository.save(sanatorium);
        return sanatoriumMapper.toDTO(savedSanatorium);

    }


    @Override
    public SanatoriumDTO updateSanatorium(Long id, SanatoriumDTO sanatoriumDTO, List<MultipartFile> attachments) {

        Sanatorium existingSanatorium = sanatoriumRepository.findById(id)
                .orElseThrow(() -> new SanatoriumNotFountException("Sanatorium not found", HttpStatus.NOT_FOUND));

        List<Attachment> attachmentEntities = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile attachment : attachments) {
                AttachmentDTO attachmentDTO = attachmentService.upload(attachment);
                Attachment attachmentEntity = attachmentMapper.toEntity(attachmentDTO);
                attachmentEntities.add(attachmentEntity);
            }
        }

        City city = cityRepository.findById(sanatoriumDTO.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        List<User> managers = new ArrayList<>();
        sanatoriumDTO.getManager().forEach(manager -> {
                    User user = userRepository.findById(manager.getId())
                            .orElseThrow(() -> new UserNotFoundException("Manager not found"));
                    managers.add(user);
                }
        );

        existingSanatorium.setName(sanatoriumDTO.getName());
        existingSanatorium.setDescription(sanatoriumDTO.getDescription());
        existingSanatorium.setAvailableSeasons(sanatoriumDTO.getAvailableSeasons());
        existingSanatorium.setCreatedBy(sanatoriumDTO.getCreatedBy());
        existingSanatorium.setTotalCapacity(sanatoriumDTO.getTotalCapacity());
        existingSanatorium.setCurrentCapacity(sanatoriumDTO.getCurrentCapacity());
        existingSanatorium.setIsActive(sanatoriumDTO.getIsActive());
        existingSanatorium.setPhoneNumber(sanatoriumDTO.getPhoneNumber());
        existingSanatorium.setLocation(sanatoriumDTO.getLocation());
        existingSanatorium.setOneDayPrice(sanatoriumDTO.getOneDayPrice());
        existingSanatorium.setTotalPrice(sanatoriumDTO.getTotalPrice());
        existingSanatorium.setCity(city);
        existingSanatorium.setManager(managers);
        existingSanatorium.setAttachment(attachmentEntities);

        Sanatorium updatedSanatorium = sanatoriumRepository.save(existingSanatorium);

        return sanatoriumMapper.toDTO(updatedSanatorium);
    }

    @Override
    public void deleteSanatoriumById(Long id) {

        Sanatorium sanatorium = sanatoriumRepository.findById(id)
                .orElseThrow(() -> new SanatoriumNotFountException("Sanatorium not found", HttpStatus.NOT_FOUND));

        // Delete the sanatorium
        sanatoriumRepository.delete(sanatorium);
    }

    @Override
    public PageDTO<SanatoriumDTO> searchSanatoriums(SanatoriumFilterDTO filterDTO, Integer page, Integer size) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // ---- Query (select)
        CriteriaQuery<Sanatorium> cq = cb.createQuery(Sanatorium.class);
        Root<Sanatorium> root = cq.from(Sanatorium.class);
        List<Predicate> predicates = generatePredicates(cb, root, filterDTO);
        cq.select(root).where(predicates.toArray(new Predicate[0])).distinct(true);

        // ---- Count query (select count)
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Sanatorium> countRoot = countQuery.from(Sanatorium.class);
        List<Predicate> countPredicates = generatePredicates(cb, countRoot, filterDTO);
        countQuery.select(cb.countDistinct(countRoot)).where(countPredicates.toArray(new Predicate[0]));
        long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        // ---- Fetch paginated content
        List<SanatoriumDTO> contentDTOList = entityManager.createQuery(cq)
                .setFirstResult(Math.max(page, 0) * size)
                .setMaxResults(size)
                .getResultList()
                .stream()
                .map(sanatoriumMapper::toDTO)
                .toList();

        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageDTO<>(
                contentDTOList,
                page,
                size,
                totalElements,
                totalPages,
                page + 1 >= totalPages,
                page == 0,
                contentDTOList.size(),
                contentDTOList.isEmpty()
        );
    }


    /**
     * Generates predicates based on the filter criteria provided in SanatoriumFilterDTO.
     * <p>
     * private String name;
     * <p>
     * private String description;
     * <p>
     * private String availableSeasons;
     * <p>
     * private Long createdBy;
     * <p>
     * private Integer totalCapacity;
     * <p>
     * private Integer currentCapacity;
     * <p>
     * private Boolean isActive;
     * <p>
     * private String phoneNumber;
     * <p>
     * private String location;
     * <p>
     * private BigDecimal oneDayPrice;
     * <p>
     * private BigDecimal totalPrice;
     * <p>
     * <p>
     * class SanatoriumFilterDTO {;
     * <p>
     * private String search; // For general search across name and description
     * <p>
     * private String name;
     * <p>
     * private String description;
     * <p>
     * private String availableSeasons;
     * <p>
     * private Integer minTotalCapacity;
     * <p>
     * private Integer maxTotalCapacity;
     * <p>
     * private Integer minCurrentCapacity;
     * <p>
     * private Integer maxCurrentCapacity;
     * <p>
     * private Boolean isActive;
     * <p>
     * private String phoneNumber;
     * <p>
     * private String location;
     * <p>
     * private BigDecimal minOneDayPrice;
     * <p>
     * private BigDecimal maxOneDayPrice;
     * <p>
     * private BigDecimal minTotalPrice;
     * <p>
     * private BigDecimal maxTotalPrice;
     * <p>
     * private String cityName; // For filtering by city name
     * <p>
     * private String managerName; // For filtering by manager name
     */
    private List<Predicate> generatePredicates(CriteriaBuilder cb, Root<Sanatorium> root, SanatoriumFilterDTO filterDTO) {

        List<Predicate> predicates = new ArrayList<>();


        if (Objects.nonNull(filterDTO.getSearch())) {
            Predicate nameLike = cb.like(root.get(Sanatorium.Fields.name), "%" + filterDTO.getSearch() + "%");
            Predicate descLike = cb.like(root.get(Sanatorium.Fields.description), "%" + filterDTO.getSearch() + "%");
            Predicate availableSeasonsLike = cb.like(root.get(Sanatorium.Fields.availableSeasons), "%" + filterDTO.getSearch() + "%");
            Predicate phoneNumberLike = cb.like(root.get(Sanatorium.Fields.phoneNumber), "%" + filterDTO.getSearch() + "%");
            Predicate locationLike = cb.like(root.get(Sanatorium.Fields.location), "%" + filterDTO.getSearch() + "%");

            predicates.add(cb.or(nameLike, descLike,
                    availableSeasonsLike, phoneNumberLike, locationLike));
        }

        if (Objects.nonNull(filterDTO.getName())) {
            predicates.add(cb.equal(root.get(Sanatorium.Fields.name), filterDTO.getName()));
        }

        if (Objects.nonNull(filterDTO.getDescription())) {
            predicates.add(cb.equal(root.get(Sanatorium.Fields.description), filterDTO.getDescription()));
        }

        if (Objects.nonNull(filterDTO.getAvailableSeasons())) {
            predicates.add(cb.like(root.get(Sanatorium.Fields.availableSeasons), "%" + filterDTO.getAvailableSeasons() + "%"));
        }

        if (Objects.nonNull(filterDTO.getMinTotalCapacity())) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Sanatorium.Fields.totalCapacity), filterDTO.getMinTotalCapacity()));
        }

        if (Objects.nonNull(filterDTO.getMaxTotalCapacity())) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Sanatorium.Fields.totalCapacity), filterDTO.getMaxTotalCapacity()));
        }

        if (Objects.nonNull(filterDTO.getMinCurrentCapacity())) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Sanatorium.Fields.currentCapacity), filterDTO.getMinCurrentCapacity()));
        }

        if (Objects.nonNull(filterDTO.getMaxCurrentCapacity())) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Sanatorium.Fields.currentCapacity), filterDTO.getMaxCurrentCapacity()));
        }

        if (Objects.nonNull(filterDTO.getIsActive())) {
            predicates.add(cb.equal(root.get(Sanatorium.Fields.isActive), filterDTO.getIsActive()));
        }

        if (Objects.nonNull(filterDTO.getPhoneNumber())) {
            predicates.add(cb.like(root.get(Sanatorium.Fields.phoneNumber), "%" + filterDTO.getPhoneNumber() + "%"));
        }

        if (Objects.nonNull(filterDTO.getLocation())) {
            predicates.add(cb.like(root.get(Sanatorium.Fields.location), "%" + filterDTO.getLocation() + "%"));
        }

        if (Objects.nonNull(filterDTO.getMinOneDayPrice())) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Sanatorium.Fields.oneDayPrice), filterDTO.getMinOneDayPrice()));
        }

        if (Objects.nonNull(filterDTO.getMaxOneDayPrice())) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Sanatorium.Fields.oneDayPrice), filterDTO.getMaxOneDayPrice()));
        }

        if (Objects.nonNull(filterDTO.getMinTotalPrice())) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(Sanatorium.Fields.totalPrice), filterDTO.getMinTotalPrice()));
        }

        if (Objects.nonNull(filterDTO.getMaxTotalPrice())) {
            predicates.add(cb.lessThanOrEqualTo(root.get(Sanatorium.Fields.totalPrice), filterDTO.getMaxTotalPrice()));
        }

        if (Objects.nonNull(filterDTO.getCityName())) {
            predicates.add(cb.like(root.join(Sanatorium.Fields.city).get(City.Fields.name), "%" + filterDTO.getCityName() + "%"));
        }

        if (Objects.nonNull(filterDTO.getManagerName())) {
            predicates.add(cb.or(
                    cb.like(root.join(Sanatorium.Fields.manager).get(User.Fields.username), "%" + filterDTO.getManagerName() + "%"),
                    cb.like(root.join(Sanatorium.Fields.manager).get(User.Fields.firstName), "%" + filterDTO.getManagerName() + "%"),
                    cb.like(root.join(Sanatorium.Fields.manager).get(User.Fields.lastName), "%" + filterDTO.getManagerName() + "%")
            ));
        }

        return predicates;
    }


}
