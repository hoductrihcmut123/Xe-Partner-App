package com.example.xepartnerapp.data_test

import com.example.xepartnerapp.data.CsoData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CsoDataTest {

    @Test
    fun `test default constructor values`() {
        // Arrange
        val csoData = CsoData()

        // Assert: Kiểm tra giá trị mặc định (null hoặc mặc định được định nghĩa trong class)
        assertNull(csoData.Cso_ID)
        assertNull(csoData.firstname)
        assertNull(csoData.lastname)
        assertNull(csoData.mobile_No)
        assertNull(csoData.business_Organization_Name)
        assertNull(csoData.email)
        assertNull(csoData.point)
        assertNull(csoData.password)
        assertNull(csoData.avatar_Link)
    }

    @Test
    fun `test constructor with specific values`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "123",
            firstname = "John",
            lastname = "Doe",
            mobile_No = "1234567890",
            business_Organization_Name = "Tech Corp",
            email = "john.doe@example.com",
            point = 100,
            password = "password123",
            avatar_Link = "http://example.com/avatar.jpg"
        )

        // Assert: Kiểm tra xem các giá trị có được gán đúng không
        assertEquals("123", csoData.Cso_ID)
        assertEquals("John", csoData.firstname)
        assertEquals("Doe", csoData.lastname)
        assertEquals("1234567890", csoData.mobile_No)
        assertEquals("Tech Corp", csoData.business_Organization_Name)
        assertEquals("john.doe@example.com", csoData.email)
        assertEquals(100, csoData.point)
        assertEquals("password123", csoData.password)
        assertEquals("http://example.com/avatar.jpg", csoData.avatar_Link)
    }

    @Test
    fun `test constructor with null values`() {
        // Arrange
        val csoData = CsoData(
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

        // Assert: Kiểm tra giá trị null cho tất cả các thuộc tính
        assertNull(csoData.Cso_ID)
        assertNull(csoData.firstname)
        assertNull(csoData.lastname)
        assertNull(csoData.mobile_No)
        assertNull(csoData.business_Organization_Name)
        assertNull(csoData.email)
        assertNull(csoData.point)
        assertNull(csoData.password)
        assertNull(csoData.avatar_Link)
    }

    @Test
    fun `test equals and hashCode methods`() {
        // Arrange
        val csoData1 = CsoData(
            Cso_ID = "123",
            firstname = "John",
            lastname = "Doe",
            mobile_No = "1234567890",
            business_Organization_Name = "Tech Corp",
            email = "john.doe@example.com",
            point = 100,
            password = "password123",
            avatar_Link = "http://example.com/avatar.jpg"
        )
        val csoData2 = CsoData(
            Cso_ID = "123",
            firstname = "John",
            lastname = "Doe",
            mobile_No = "1234567890",
            business_Organization_Name = "Tech Corp",
            email = "john.doe@example.com",
            point = 100,
            password = "password123",
            avatar_Link = "http://example.com/avatar.jpg"
        )

        // Assert: Kiểm tra nếu hai đối tượng CsoData này là equal (được tạo giống nhau)
        assertTrue(csoData1 == csoData2)
        assertEquals(csoData1.hashCode(), csoData2.hashCode())
    }

    @Test
    fun `test copy method`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "123",
            firstname = "John",
            lastname = "Doe",
            mobile_No = "1234567890",
            business_Organization_Name = "Tech Corp",
            email = "john.doe@example.com",
            point = 100,
            password = "password123",
            avatar_Link = "http://example.com/avatar.jpg"
        )

        // Act: Sử dụng method copy để tạo đối tượng mới
        val copiedCsoData = csoData.copy(firstname = "Jane")

        // Assert: Kiểm tra xem đối tượng sao chép có giá trị đúng như mong đợi không
        assertEquals("123", copiedCsoData.Cso_ID)
        assertEquals("Jane", copiedCsoData.firstname)  // Thay đổi firstname
        assertEquals("Doe", copiedCsoData.lastname)
        assertEquals("1234567890", copiedCsoData.mobile_No)
        assertEquals("Tech Corp", copiedCsoData.business_Organization_Name)
        assertEquals("john.doe@example.com", copiedCsoData.email)
        assertEquals(100, copiedCsoData.point)
        assertEquals("password123", copiedCsoData.password)
        assertEquals("http://example.com/avatar.jpg", copiedCsoData.avatar_Link)
    }

    // Test với giá trị đầu vào hợp lệ
    @Test
    fun `test constructor with valid input`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C001",
            firstname = "Alice",
            lastname = "Smith",
            mobile_No = "0987654321",
            business_Organization_Name = "Green Tech",
            email = "alice.smith@example.com",
            point = 150,
            password = "securePass123",
            avatar_Link = "http://example.com/avatar.jpg"
        )

        // Assert: Kiểm tra giá trị các thuộc tính
        assertEquals("C001", csoData.Cso_ID)
        assertEquals("Alice", csoData.firstname)
        assertEquals("Smith", csoData.lastname)
        assertEquals("0987654321", csoData.mobile_No)
        assertEquals("Green Tech", csoData.business_Organization_Name)
        assertEquals("alice.smith@example.com", csoData.email)
        assertEquals(150, csoData.point)
        assertEquals("securePass123", csoData.password)
        assertEquals("http://example.com/avatar.jpg", csoData.avatar_Link)
    }

    // Test với thông tin email không hợp lệ
    @Test
    fun `test invalid email`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C002",
            firstname = "Bob",
            lastname = "Johnson",
            mobile_No = "0987654321",
            business_Organization_Name = "Tech Innovators",
            email = "bob.johnson@",
            point = 200,
            password = "password456",
            avatar_Link = "http://example.com/avatar2.jpg"
        )

        // Assert: Kiểm tra email có được cung cấp đúng định dạng không
        assertEquals("bob.johnson@", csoData.email)
    }

    // Test với giá trị `null` cho `mobile_No`
    @Test
    fun `test null mobile_No`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C003",
            firstname = "Charlie",
            lastname = "Brown",
            mobile_No = null,
            business_Organization_Name = "Mobility Solutions",
            email = "charlie.brown@example.com",
            point = 180,
            password = "password789",
            avatar_Link = "http://example.com/avatar3.jpg"
        )

        // Assert: Kiểm tra `mobile_No` có phải là null
        assertNull(csoData.mobile_No)
    }

    // Test khi avatar_Link bị thiếu
    @Test
    fun `test missing avatar_Link`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C004",
            firstname = "David",
            lastname = "Williams",
            mobile_No = "1234567890",
            business_Organization_Name = "Tech Masters",
            email = "david.williams@example.com",
            point = 120,
            password = "password123",
            avatar_Link = null
        )

        // Assert: Kiểm tra xem avatar_Link có phải là null hay không
        assertNull(csoData.avatar_Link)
    }

    // Test với điểm = 0
    @Test
    fun `test zero points`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C005",
            firstname = "Eve",
            lastname = "Davis",
            mobile_No = "2345678901",
            business_Organization_Name = "Business Corp",
            email = "eve.davis@example.com",
            point = 0,
            password = "evePass123",
            avatar_Link = "http://example.com/avatar4.jpg"
        )

        // Assert: Kiểm tra điểm là 0
        assertEquals(0, csoData.point)
    }

    // Test với việc copy dữ liệu và thay đổi một thuộc tính
    @Test
    fun `test copy with modified property`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C006",
            firstname = "Frank",
            lastname = "Taylor",
            mobile_No = "3456789012",
            business_Organization_Name = "Innovation Co",
            email = "frank.taylor@example.com",
            point = 250,
            password = "frankPass123",
            avatar_Link = "http://example.com/avatar5.jpg"
        )

        // Act: Sao chép và thay đổi `email`
        val copiedCsoData = csoData.copy(email = "new.frank@example.com")

        // Assert: Kiểm tra thuộc tính `email` được thay đổi đúng
        assertEquals("C006", copiedCsoData.Cso_ID)
        assertEquals("Frank", copiedCsoData.firstname)
        assertEquals("new.frank@example.com", copiedCsoData.email)  // Đảm bảo email đã thay đổi
    }

    // Test trường hợp điểm âm
    @Test
    fun `test negative points`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C007",
            firstname = "Grace",
            lastname = "Miller",
            mobile_No = "4567890123",
            business_Organization_Name = "FinTech Solutions",
            email = "grace.miller@example.com",
            point = -10,
            password = "gracePass123",
            avatar_Link = "http://example.com/avatar6.jpg"
        )

        // Assert: Kiểm tra điểm âm
        assertEquals(-10, csoData.point)
    }

    // Test với tất cả thuộc tính là null
    @Test
    fun `test all null values`() {
        // Arrange
        val csoData = CsoData(
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

        // Assert: Kiểm tra tất cả thuộc tính đều là null
        assertNull(csoData.Cso_ID)
        assertNull(csoData.firstname)
        assertNull(csoData.lastname)
        assertNull(csoData.mobile_No)
        assertNull(csoData.business_Organization_Name)
        assertNull(csoData.email)
        assertNull(csoData.point)
        assertNull(csoData.password)
        assertNull(csoData.avatar_Link)
    }

    // Test khi `firstname` hoặc `lastname` chứa ký tự đặc biệt
    @Test
    fun `test special characters in names`() {
        // Arrange
        val csoData = CsoData(
            Cso_ID = "C008",
            firstname = "José",
            lastname = "O'Connor",
            mobile_No = "5678901234",
            business_Organization_Name = "Global Solutions",
            email = "jose.connor@example.com",
            point = 500,
            password = "josePass123",
            avatar_Link = "http://example.com/avatar7.jpg"
        )

        // Assert: Kiểm tra các ký tự đặc biệt trong `firstname` và `lastname`
        assertEquals("José", csoData.firstname)
        assertEquals("O'Connor", csoData.lastname)
    }
}
