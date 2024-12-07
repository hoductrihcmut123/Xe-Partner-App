package com.example.xepartnerapp.repository

import com.example.xepartnerapp.data.DriverData

object DriverMock {

    // Dữ liệu mock cho DriverData
    fun getDriverMockData(): DriverData {
        return DriverData(
            driver_ID = "driver123",
            firstname = "John",
            lastname = "Doe",
            email = "johndoe@example.com",
            gender = true, // true for male, false for female
            mobile_No = "1234567890",
            point = 100,
            password = "password123",
            avatar_Link = "https://example.com/avatar.jpg",
            vehicle_Link = "https://example.com/vehicle.jpg",
            card_ID = "card123",
            license = "ABC123",
            classify = "SUV",
            machine_Number = "1234567890",
            license_Plate = "XYZ-1234",
            place_Manufacture = "USA",
            vehicle_Color = "Red",
            vehicle_Line = "Sedan",
            seat_Num = 4,
            year_Manufacture = 2020,
            vehicle_Brand = "Toyota",
            ready = true,  // true if ready for ride
            totalStar = 5,
            rateStarNum = 10,
            completeTripNum = 50,
            totalDistance = 10000,  // in kilometers
            momoPhone = "9876543210"
        )
    }

    // Dữ liệu mock cho DriverData với một ID khác
    fun getAnotherDriverMockData(): DriverData {
        return DriverData(
            driver_ID = "driver456",
            firstname = "Jane",
            lastname = "Smith",
            email = "janesmith@example.com",
            gender = false, // false for female
            mobile_No = "9876543210",
            point = 120,
            password = "password456",
            avatar_Link = "https://example.com/avatar2.jpg",
            vehicle_Link = "https://example.com/vehicle2.jpg",
            card_ID = "card456",
            license = "DEF456",
            classify = "Sedan",
            machine_Number = "0987654321",
            license_Plate = "ABC-5678",
            place_Manufacture = "Japan",
            vehicle_Color = "Blue",
            vehicle_Line = "Hatchback",
            seat_Num = 5,
            year_Manufacture = 2019,
            vehicle_Brand = "Honda",
            ready = false,  // false if not ready for ride
            totalStar = 4,
            rateStarNum = 8,
            completeTripNum = 30,
            totalDistance = 5000,  // in kilometers
            momoPhone = "1122334455"
        )
    }
}
