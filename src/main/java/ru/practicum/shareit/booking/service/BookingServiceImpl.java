package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.dao.BookingRepository;

import ru.practicum.shareit.exception.ForbiddenAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoOut add(Long userId, BookingDto bookingDto) {
        User user = UserMapper.fromDto(userService.getUserById(userId));
        Optional<Item> itemById = itemRepository.findById(bookingDto.getItemId());
        if (itemById.isEmpty()) {
            throw new NotFoundException("Вещь не найдена.");
        }
        Item item = itemById.get();

        log.debug("Полученный пользователь: {}", user);
        log.debug("Найденный предмет: {}", item);

        bookingValidation(bookingDto, user, item);
        validateNoTimeConflict(item, bookingDto); // Новая проверка на пересечение времени

        Booking booking = BookingMapper.toBooking(user, item, bookingDto);

        log.debug("Создана новая бронь: {}", booking);

        return BookingMapper.toBookingOut(bookingRepository.save(booking));
    }


    @Override
    @Transactional
    public BookingDtoOut update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = validateBookingDetailsForOwner(userId, bookingId);
        log.debug("Текущая бронь: {}", booking);
        log.debug("Новый статус: {}", approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        BookingStatus newStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(newStatus);
        return BookingMapper.toBookingOut(bookingRepository.save(booking));
    }


    @Override
    @Transactional
    public BookingDtoOut findBookingByUserId(Long userId, Long bookingId) {
        Booking booking = validateBookingDetailsForRole(userId, bookingId);
        log.debug("Найденная бронь: {}", booking);
        return BookingMapper.toBookingOut(booking);
    }


    @Override
    @Transactional
    public List<BookingDtoOut> findAll(Long bookerId, String state) {
        userService.getUserById(bookerId);

        LocalDateTime now = LocalDateTime.now();
        Function<List<Booking>, List<BookingDtoOut>> mapper = bookings -> bookings.stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
        log.debug("Поиск бронирований для пользователя с ID: {}, состояние: {}", bookerId, state);

        return switch (validState(state)) {
            case ALL -> mapper.apply(bookingRepository.findAllBookingsByBookerId(bookerId));
            case CURRENT -> mapper.apply(bookingRepository.findAllCurrentBookingsByBookerId(bookerId, now));
            case PAST -> mapper.apply(bookingRepository.findAllPastBookingsByBookerId(bookerId, now));
            case FUTURE -> mapper.apply(bookingRepository.findAllFutureBookingsByBookerId(bookerId, now));
            case WAITING -> mapper.apply(bookingRepository.findAllWaitingBookingsByBookerId(bookerId, now));
            case REJECTED -> mapper.apply(bookingRepository.findAllRejectedBookingsByBookerId(bookerId, now));
        };
    }


    @Override
    @Transactional
    public List<BookingDtoOut> findAllOwner(Long ownerId, BookingState state) {
        userService.getUserById(ownerId);
        log.debug("Поиск бронирований для владельца с ID: {}, состояние: {}", ownerId, state);

        return switch (state) {
            case ALL -> findAllBookingsOwner(ownerId);
            case CURRENT -> findCurrentBookingsOwner(ownerId, LocalDateTime.now());
            case PAST -> findPastBookingsOwner(ownerId, LocalDateTime.now());
            case FUTURE -> findFutureBookingsOwner(ownerId, LocalDateTime.now());
            case WAITING -> findWaitingBookingsOwner(ownerId, LocalDateTime.now());
            case REJECTED -> findRejectedBookingsOwner(ownerId);
        };
    }


    private List<BookingDtoOut> findAllBookingsOwner(Long ownerId) {
        return bookingRepository.findAllBookingsByOwnerId(ownerId).stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
    }

    private List<BookingDtoOut> findCurrentBookingsOwner(Long ownerId, LocalDateTime now) {
        return bookingRepository.findAllCurrentBookingsByOwnerId(ownerId, now).stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
    }

    private List<BookingDtoOut> findPastBookingsOwner(Long ownerId, LocalDateTime now) {
        return bookingRepository.findAllPastBookingsByOwnerId(ownerId, now).stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
    }

    private List<BookingDtoOut> findFutureBookingsOwner(Long ownerId, LocalDateTime now) {
        return bookingRepository.findAllFutureBookingsByOwnerId(ownerId, now).stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
    }

    private List<BookingDtoOut> findWaitingBookingsOwner(Long ownerId, LocalDateTime now) {
        return bookingRepository.findAllWaitingBookingsByOwnerId(ownerId, now).stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
    }

    private List<BookingDtoOut> findRejectedBookingsOwner(Long ownerId) {
        return bookingRepository.findAllRejectedBookingsByOwnerId(ownerId).stream()
                .map(BookingMapper::toBookingOut)
                .collect(Collectors.toList());
    }


    private void validateItemAvailability(Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования.");
        }
    }

    private void validateUserIsNotOwner(User user, Item item) {
        if (user.getId().equals(item.getOwner().getId())) {
            throw new ForbiddenAccessException("Пользователь не может выполнить действие с вещью," +
                                               " так как является её владельцем");
        }
    }

    private void validateDates(BookingDto bookingDto) {
        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new ValidationException("Дата окончания не может быть раньше или равна дате начала");
        }
    }

    private void bookingValidation(BookingDto bookingDto, User user, Item item) {
        validateItemAvailability(item);
        validateUserIsNotOwner(user, item);
        validateDates(bookingDto);
    }


    private BookingState validState(String bookingState) {
        BookingState state = BookingState.from(bookingState);
        if (state == null) {
            throw new IllegalArgumentException("Unknown state: " + bookingState);
        }
        return state;
    }

    private Booking validateBookingDetailsForOwner(Long userId, Long bookingId) {
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);
        if (bookingById.isEmpty()) {
            throw new NotFoundException("Бронь с идентификатором " + bookingId + " не найдена.");
        }
        Booking booking = bookingById.get();
        validateOwnerAndStatus(booking, userId);
        return booking;
    }

    private Booking validateBookingDetailsForRole(Long userId, Long bookingId) {
        Optional<Booking> bookingById = bookingRepository.findById(bookingId);
        if (bookingById.isEmpty()) {
            throw new NotFoundException("Бронь с идентификатором " + bookingId + " не найдена.");
        }
        Booking booking = bookingById.get();
        validateUserRole(booking, userId);
        return booking;
    }

    private void validateOwnerAndStatus(Booking booking, Long userId) {
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenAccessException("Пользователь не является владельцем");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Бронь cо статусом WAITING");
        }
    }

    private void validateUserRole(Booking booking, Long userId) {
        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("Пользователь не владелиц и не автор бронирования");
        }
    }

    private void validateNoTimeConflict(Item item, BookingDto bookingDto) {
        List<Booking> existingBookings = bookingRepository.findAll();
        for (Booking existingBooking : existingBookings) {
            if (existingBooking.getItem().getId().equals(item.getId())) {
                if (bookingDto.getStart().isAfter(existingBooking.getStart())
                    && bookingDto.getEnd().isBefore(existingBooking.getEnd())) {
                    throw new ValidationException("Вещь уже забронирована на указанный период.");
                }
            }
        }
    }
}