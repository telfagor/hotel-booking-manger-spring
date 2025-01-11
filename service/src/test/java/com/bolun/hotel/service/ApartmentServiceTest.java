package com.bolun.hotel.service;

import com.bolun.hotel.dto.ApartmentCreateEditDto;
import com.bolun.hotel.dto.ApartmentReadDto;
import com.bolun.hotel.dto.filters.ApartmentFilter;
import com.bolun.hotel.entity.Apartment;
import com.bolun.hotel.integration.util.TestObjectsUtils;
import com.bolun.hotel.mapper.ApartmentCreateEditMapper;
import com.bolun.hotel.mapper.ApartmentReadMapper;
import com.bolun.hotel.repository.ApartmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ApartmentServiceTest {

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private ApartmentReadMapper apartmentReadMapper;

    @Mock
    private ApartmentCreateEditMapper apartmentCreateEditMapper;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ApartmentService apartmentService;

    @Test
    void findAll() {
        ApartmentFilter filter = TestObjectsUtils.getApartmentFilter();
        Apartment apartment1 = TestObjectsUtils.getApartment();
        Apartment apartment2 = TestObjectsUtils.getApartment2();
        ApartmentReadDto apartmentReadDto1 = TestObjectsUtils.getApartmentReadDto();
        ApartmentReadDto apartmentReadDto2 = TestObjectsUtils.getApartmentReadDto2();
        List<Apartment> apartments = List.of(apartment1, apartment2);
        Page<Apartment> apartmentPage = new PageImpl<>(apartments, PageRequest.of(0, 2), apartments.size());
        doReturn(apartmentPage).when(apartmentRepository).findAll(eq(filter), any(Pageable.class));
        doReturn(apartmentReadDto1).when(apartmentReadMapper).mapFrom(apartment1);
        doReturn(apartmentReadDto2).when(apartmentReadMapper).mapFrom(apartment2);

        Page<ApartmentReadDto> actualResult = apartmentService.findAll(filter, PageRequest.of(0, 2));

        assertThat(actualResult.getContent()).containsExactlyInAnyOrder(apartmentReadDto1, apartmentReadDto2);
        assertThat(actualResult.getTotalElements()).isEqualTo(apartments.size());
        assertThat(actualResult.getNumber()).isZero();
        assertThat(actualResult.getSize()).isEqualTo(2);
        verify(apartmentRepository).findAll(eq(filter), any(Pageable.class));
        verify(apartmentReadMapper).mapFrom(apartment1);
        verify(apartmentReadMapper).mapFrom(apartment2);
    }




    @Test
    void shouldNotFindById() {
        UUID fakeId = UUID.randomUUID();
        doReturn(Optional.empty()).when(apartmentRepository).findActiveById(fakeId);

        Optional<ApartmentReadDto> actualResult = apartmentService.findById(fakeId);

        assertThat(actualResult).isEmpty();
        verifyNoInteractions(apartmentReadMapper);
    }

    @Test
    void create() {
        ApartmentService spyApartmentService = Mockito.spy(apartmentService);
        MockMultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        Apartment apartment = TestObjectsUtils.getApartment();
        ApartmentReadDto apartmentReadDto = TestObjectsUtils.getApartmentReadDto();
        doNothing().when(spyApartmentService).uploadImage(mockPhoto);
        doReturn(apartment).when(apartmentCreateEditMapper).mapFrom(apartmentCreateEditDto);
        doReturn(apartment).when(apartmentRepository).save(apartment);
        doReturn(apartmentReadDto).when(apartmentReadMapper).mapFrom(apartment);

        ApartmentReadDto actualResult = spyApartmentService.create(apartmentCreateEditDto);

        assertThat(actualResult).isEqualTo(apartmentReadDto);
        verify(apartmentCreateEditMapper).mapFrom(apartmentCreateEditDto);
        verify(apartmentRepository).save(apartment);
        verify(apartmentReadMapper).mapFrom(apartment);
    }

    @Test
    void shouldThrowAnExceptionWhenCreate() {
        MockMultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        doReturn(null).when(apartmentCreateEditMapper).mapFrom(apartmentCreateEditDto);

        assertThrows(NoSuchElementException.class, () -> apartmentService.create(apartmentCreateEditDto));

        verifyNoInteractions(apartmentRepository);
        verifyNoInteractions(apartmentReadMapper);
    }

    @Test
    void update() {
        Apartment apartment = TestObjectsUtils.getApartment();
        ApartmentService spyApartmentService = Mockito.spy(apartmentService);
        MockMultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        ApartmentReadDto apartmentReadDto = TestObjectsUtils.getApartmentReadDto();
        doReturn(Optional.of(apartment)).when(apartmentRepository).findActiveByIdWithLock(apartment.getId());
        doNothing().when(spyApartmentService).uploadImage(mockPhoto);
        doReturn(apartment).when(apartmentCreateEditMapper).mapFrom(apartmentCreateEditDto, apartment);
        doReturn(apartment).when(apartmentRepository).saveAndFlush(apartment);
        doReturn(apartmentReadDto).when(apartmentReadMapper).mapFrom(apartment);

        Optional<ApartmentReadDto> actualResult = spyApartmentService.update(apartment.getId(), apartmentCreateEditDto);

        assertThat(actualResult)
                .isPresent()
                .contains(apartmentReadDto);
        verify(apartmentRepository).findActiveByIdWithLock(apartment.getId());
        verify(apartmentCreateEditMapper).mapFrom(apartmentCreateEditDto, apartment);
        verify(apartmentRepository).saveAndFlush(apartment);
        verify(apartmentReadMapper).mapFrom(apartment);
    }

    @Test
    void shouldReturnAnEmptyOptionalWhenUpdateFails() {
        Apartment apartment = TestObjectsUtils.getApartment();
        MockMultipartFile mockPhoto = new MockMultipartFile("image.png", new byte[0]);
        ApartmentCreateEditDto apartmentCreateEditDto = TestObjectsUtils.getApartmentCreateEditDto(mockPhoto);
        UUID fakeId = UUID.randomUUID();
        doReturn(Optional.empty()).when(apartmentRepository).findActiveByIdWithLock(fakeId);

        Optional<ApartmentReadDto> maybeApartment = apartmentService.update(fakeId, apartmentCreateEditDto);

        assertThat(maybeApartment).isEmpty();
        verify(apartmentRepository).findActiveByIdWithLock(fakeId);
        verifyNoInteractions(apartmentCreateEditMapper);
        verify(apartmentRepository, never()).saveAndFlush(apartment);
        verifyNoInteractions(apartmentReadMapper);
    }

    @Test
    void findPhoto() {
        String photoPath = "path/to/photo.png";
        byte[] photoBytes = new byte[] {1, 2, 3};
        Apartment apartment = new Apartment();
        apartment.setPhoto(photoPath);
        doReturn(Optional.of(apartment)).when(apartmentRepository).findById(apartment.getId());
        doReturn(Optional.of(photoBytes)).when(imageService).get(photoPath);

        Optional<byte[]> actualResult = apartmentService.findPhoto(apartment.getId());

        assertThat(actualResult)
                .isPresent()
                .contains(photoBytes);
        verify(apartmentRepository).findById(apartment.getId());
        verify(imageService).get(photoPath);
    }

    @Test
    void shouldNotFindPhoto() {
        Apartment apartment = TestObjectsUtils.getApartment();
        doReturn(Optional.of(apartment)).when(apartmentRepository).findById(apartment.getId());

        Optional<byte[]> actualResult = apartmentService.findPhoto(apartment.getId());

        assertThat(actualResult).isEmpty();
        verify(apartmentRepository).findById(apartment.getId());
        verify(imageService, never()).get(null);
    }

    @Test
    void delete() {
        Apartment apartment = TestObjectsUtils.getApartment();
        doReturn(Optional.of(apartment)).when(apartmentRepository).findActiveByIdWithLock(apartment.getId());
        doNothing().when(apartmentRepository).flush();

        boolean actualResult = apartmentService.delete(apartment.getId());

        assertTrue(actualResult);
        verify(apartmentRepository).findActiveByIdWithLock(apartment.getId());
        verify(apartmentRepository).flush();
    }

    @Test
    void shouldReturnFalseWhenDeleteFails() {
        Apartment apartment = TestObjectsUtils.getApartment();
        doReturn(Optional.empty()).when(apartmentRepository).findActiveByIdWithLock(apartment.getId());

        boolean actualResult = apartmentService.delete(apartment.getId());

        assertFalse(actualResult);
        verify(apartmentRepository).findActiveByIdWithLock(apartment.getId());
        verify(apartmentRepository, never()).flush();
    }
}
