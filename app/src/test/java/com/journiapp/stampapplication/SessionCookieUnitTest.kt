package com.journiapp.stampapplication

import com.journiapp.stampapplication.data.network.NetworkUtil
import okhttp3.Headers
import org.junit.Assert
import org.junit.Test

class SessionCookieUnitTest {

    @Test
    fun `Session cookie is returned`() {
        val sessionCookie = "session=ABCDE"
        val headers = Headers.headersOf("Set-Cookie", sessionCookie)
        val actualSessionCookie = NetworkUtil.getCookieString(headers)
        Assert.assertEquals(sessionCookie, actualSessionCookie)
    }

    @Test
    fun `Session cookie with attributes is also returned`() {
        val sessionCookie = "session=ABCDE"
        val headers = Headers.headersOf("Set-Cookie", "$sessionCookie; Max-Age=311040000; Expires=Thu, 29 Jul 2100 13:07:15 GMT; Path=/; Secure; HTTPOnly")
        val actualSessionCookie = NetworkUtil.getCookieString(headers)
        Assert.assertEquals(sessionCookie, actualSessionCookie)
    }

    /**
     * TODO: Our network layer should only use `session` cookies, but there is currently a bug where
     *  any kind of cookie is accepted. Fix the `NetworkUtil.getCookieString` to only return
     *  `session` cookies.
     */
    @Test
    fun `Other non session cookies are not returned`() {
        val nonSessionCookie = "NonSessionCookie=XYZ"
        val headers = Headers.headersOf("Set-Cookie", nonSessionCookie)
        val actualSessionCookie = NetworkUtil.getCookieString(headers)
        Assert.assertNull(actualSessionCookie)
    }
}