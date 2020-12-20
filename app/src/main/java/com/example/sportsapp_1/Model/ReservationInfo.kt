package com.example.sportsapp_1.Model

import android.widget.SeekBar

data class ReservationInfo (var reservationHour : String = "",
                            var reservationCurrent : Int = 0,
                            var reservationQuota : Int = 0,
                            )