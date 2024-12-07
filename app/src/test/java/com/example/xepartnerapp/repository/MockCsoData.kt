package com.example.xepartnerapp.repository

import com.example.xepartnerapp.data.CsoData

// Mock dữ liệu cho CsoData
object MockCsoData {
    val validCsoData = CsoData(
        Cso_ID = "C001",
        firstname = "John",
        lastname = "Doe",
        mobile_No = "0123456789",
        business_Organization_Name = "TechCorp",
        email = "john.doe@example.com",
        point = 100,
        password = "securePassword123",
        avatar_Link = "https://www.example.com/avatar.jpg"
    )

    val anotherValidCsoData = CsoData(
        Cso_ID = "C002",
        firstname = "Jane",
        lastname = "Smith",
        mobile_No = "0987654321",
        business_Organization_Name = "WebSolutions",
        email = "jane.smith@example.com",
        point = 200,
        password = "anotherPassword456",
        avatar_Link = "https://www.example.com/avatar2.jpg"
    )

    // Mock dữ liệu với các giá trị null
    val invalidCsoData = CsoData(
        Cso_ID = null,
        firstname = null,
        lastname = null,
        mobile_No = null,
        business_Organization_Name = null,
        email = null,
        point = null,
        password = null,
        avatar_Link = null
    )
}
