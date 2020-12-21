package com.example.sportsapp_1.Model


data class ReservationInfo (var reservationHour : String = "",
                            var reservationCurrent : Int = 0,
                            var reservationQuota : Int = 10,
                            var reservationDate: String ="",
                            var reservationId: String = "")