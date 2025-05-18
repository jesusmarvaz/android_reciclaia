package com.ingencode.reciclaia.data.remote.api

/**
Created with ❤ by Jesús Martín (jesusmarvaz@gmail.com) on 2025-02-03.
 */

object Routes {
    const val SUPPORT_EMAIL = "test@test.es"
    const val BASE_URL = "https://jesusmarvaz.hopto.org/apis/reciclaia/"
    private const val BASE_URL_WEB = "https://jesusmarvaz.hopto.org/web/"
    const val WEB_TERMS = "${BASE_URL_WEB}terms"
    const val WEB_PRIVACY = "${BASE_URL_WEB}privacy"
    const val IMAGES_PATH = BASE_URL_WEB

    object PathParams {
        const val ID = "id"
    }

    object Headers {
        const val TOKEN_HEADER = "authorization"
    }

    object ApisEndpoints {
        object TestApi {
            const val TEST = "test"
            const val TESTDB = "testdb"
        }
        object WasteInfo {
            private const val FOLDER = "getwasteinfo/"
            const val WASTE_TYPES = "${FOLDER}waste_type.json"
            const val PROCESSING_TYPES = "${FOLDER}processing_type.json"
            const val URLS = "${FOLDER}urls.json"
        }
    }
}