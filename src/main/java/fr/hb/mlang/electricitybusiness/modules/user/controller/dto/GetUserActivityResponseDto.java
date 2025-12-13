package fr.hb.mlang.electricitybusiness.modules.user.controller.dto;

public record GetUserActivityResponseDto(
    // locations[] (id, address, city, postalCode, stations[](id, label, available))
    // bookings[] (id, start, end, status, station, customer)
) {

}
