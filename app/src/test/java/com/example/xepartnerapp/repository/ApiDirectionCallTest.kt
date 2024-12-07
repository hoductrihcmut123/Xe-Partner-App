package com.example.xepartnerapp.repository

import android.net.Uri
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock

class ApiCallTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var url: String

    @Mock
    private lateinit var requestQueue: RequestQueue

    @Before
    fun setup() {
        // Setup MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Construct the URL for testing
        url = mockWebServer.url("/maps/api/directions/json").toString()

        // Mocking RequestQueue
        requestQueue = mock(RequestQueue::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testVolleyApiCall() {
        // Prepare the mock response from MockWebServer
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                    "status": "OK",
                    "routes": []
                }
                """.trimIndent()
            )

        // Enqueue the mock response
        mockWebServer.enqueue(mockResponse)

        // Simulate the API call
        val destination = Location(37.7749, -122.4194)
        val origin = Location(34.0522, -118.2437)
        val mode = "driving"

        // Call API method
        makeApiCall(destination, origin, mode)

        // Verify the response was handled correctly
        val request = mockWebServer.takeRequest()
        assert(request.method == "GET")
        assert(request.path?.contains("destination=37.7749%2C-122.4194") == true)
        assert(request.path?.contains("origin=34.0522%2C-118.2437") == true)
    }

    private fun makeApiCall(destination: Location, origin: Location, mode: String) {
        val baseUrl = "https://maps.googleapis.com/maps/api/directions/json"

        val uri = Uri.parse(baseUrl)
        if (uri == null) {
            Log.e("API Error", "Invalid URL")
            return
        }

        val fullUrl = uri.buildUpon()
            .appendQueryParameter(
                "destination",
                "${destination.latitude}, ${destination.longitude}"
            )
            .appendQueryParameter("origin", "${origin.latitude}, ${origin.longitude}")
            .appendQueryParameter("mode", mode)
            .appendQueryParameter("key", "YOUR_API_KEY")
            .toString()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            fullUrl,
            null,
            { response ->
                // Handle response
            },
            { error ->
                // Handle error
            }
        )

        // Add the request to the requestQueue
        requestQueue.add(jsonObjectRequest)
    }

    // Mock Location data class
    data class Location(val latitude: Double, val longitude: Double)
}
