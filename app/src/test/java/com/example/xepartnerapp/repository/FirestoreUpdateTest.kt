package com.example.xepartnerapp.repository

import com.example.xepartnerapp.data.DriverData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.Awaits
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class FirestoreUpdateTest {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var mockDriverData: DriverData

    @Before
    fun setup() {
        // Khởi tạo dữ liệu mock cho DriverData
        mockDriverData = DriverMock.getDriverMockData()

        // Khởi tạo mock Firestore
        firestore = mockk()

        mockkStatic(FirebaseFirestore::class)
    }

    @Test
    fun `test update trip_ID field in Firestore`() {
        val driverID = mockDriverData.driver_ID
        val documentReference = mockk<DocumentReference>()
        every { firestore.collection("Drivers").document(driverID!!) } returns documentReference

        every { documentReference.update(any<Map<String, Any>>()) } just Awaits

        val ref = driverID?.let { firestore.collection("Drivers").document(it) }
        ref?.update(
            mapOf(
                "trip_ID" to FieldValue.delete()
            )
        )

        verify {
            documentReference.update(
                mapOf(
                    "trip_ID" to FieldValue.delete()
                )
            )
        }
    }
}
