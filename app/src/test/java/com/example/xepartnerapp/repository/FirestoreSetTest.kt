package com.example.xepartnerapp.repository

import com.example.xepartnerapp.data.CsoData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.jupiter.api.Test

class FirestoreSetTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var collection: CollectionReference
    private lateinit var mockCsoData: CsoData

    @Before
    fun setup() {
        // Mock Firestore và CollectionReference
        firestore = mockk()
        collection = mockk()

        // Mock method gọi firestore.collection()
        every { firestore.collection(any()) } returns collection

        // Mock set() method
        every { collection.document(any()).set(any(), any()) } returns mockk()

        // Khởi tạo dữ liệu mock
        mockCsoData = MockCsoData.validCsoData
    }

    @Test
    fun testSetCsoData() {
        // Arrange
        val userId = "C001" // Sử dụng ID của người dùng mock
        val expectedCsoData = mockCsoData

        // Act
        collection.document(userId).set(expectedCsoData)

        // Assert
        verify { collection.document(userId).set(expectedCsoData, SetOptions.merge()) }
    }

    @Test
    fun testSetCsoDataWhenEmptyFields() {
        // Arrange
        val userId = "C002"
        val emptyCsoData = MockCsoData.invalidCsoData

        // Act
        collection.document(userId).set(emptyCsoData)

        // Assert
        verify { collection.document(userId).set(emptyCsoData, SetOptions.merge()) }
    }
}
