package com.test.geneatexls

import com.fasterxml.jackson.databind.ObjectMapper
import com.service.generatexls.GeneratexlsApplication
import com.service.generatexls.controllers.MainController
import com.service.generatexls.controllers.RestExceptionHandler
import com.service.generatexls.dto.Event
import com.service.generatexls.service.CheckAuthService
import com.service.generatexls.service.GenerateXlsService
import com.service.generatexls.service.RestTemplateGetJson
import org.apache.poi.xssf.usermodel.XSSFWorkbookType
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.web.client.ResourceAccessException
import java.io.File
import java.net.URL

@SpringBootTest(classes = [GeneratexlsApplication::class])
class GenerateXlsServiceTest {
    val end = "302020-05-04"
    val begin = "302020-01-01"

    @Test
    fun whenTheEmptyAnswer_returnNoContentStatusCode() {
        val mockRestTemplateGetJson = Mockito.mock(RestTemplateGetJson::class.java)
        val mockCheckAuthService = Mockito.mock(CheckAuthService::class.java)
        Mockito.`when`(mockRestTemplateGetJson.getJson(end, begin)).thenReturn(ArrayList<Event>())
        val result = MainController(RestExceptionHandler(), GenerateXlsService(), mockRestTemplateGetJson, mockCheckAuthService).downloadTemplate("", end, begin)
        assert(result.statusCode.value() == 204) { "the answer is not empty" }

    }

    @Test
    fun whenBackEndNotAvailable_returnInternalServerError() {
        val mockRestTemplateGetJson = Mockito.mock(RestTemplateGetJson::class.java)
        val mockCheckAuthService = Mockito.mock(CheckAuthService::class.java)
        Mockito.`when`(mockRestTemplateGetJson.getJson(end, begin)).thenThrow(ResourceAccessException::class.java)
        val result = MainController(RestExceptionHandler(), GenerateXlsService(), mockRestTemplateGetJson, mockCheckAuthService).downloadTemplate("", end, begin)
        assert(result.statusCode.value() == 500) { "Back end available" }
    }

    @Test
    fun whenServiceWorkingFine() {

        val dirUrl: URL = ClassLoader.getSystemResource("source.json")
        val dir = File(dirUrl.toURI())
        val mapper = ObjectMapper()
        val type = mapper.getTypeFactory().constructCollectionType(List::class.java, Event::class.java)
        val list: List<Event> = mapper.readValue(dir, type)
        val mockRestTemplateGetJson = Mockito.mock(RestTemplateGetJson::class.java)
        val mockCheckAuthService = Mockito.mock(CheckAuthService::class.java)
        Mockito.`when`(mockRestTemplateGetJson.getJson(end, begin)).thenReturn(list)
        val result = GenerateXlsService().getXls(mockRestTemplateGetJson.getJson(end, begin))
        val result1 = MainController(RestExceptionHandler(), GenerateXlsService(), mockRestTemplateGetJson, mockCheckAuthService).downloadTemplate("", end, begin)
        assert(result.workbookType == XSSFWorkbookType.XLSX)
        assert(result1.statusCode.value() == 200)
        assert(result1.headers.contentType == MediaType("xls", "force-download")) { "Service not working fine" }
    }
}










