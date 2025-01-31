package com.review.study.issue.auto.application

import com.review.study.issue.auto.utils.weekOfYear
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class TemplateService {
    private final val YYYY_MM_DD_FORMAT = "yyyy.MM.dd"


    fun createIssueTitle(username: String, zoneId: ZoneId = ZoneId.of("Asia/Seoul")): String {
        val now = ZonedDateTime.now(zoneId)

        val monday = now.with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern(YYYY_MM_DD_FORMAT))
        val sunday = now.with(DayOfWeek.SUNDAY).format(DateTimeFormatter.ofPattern(YYYY_MM_DD_FORMAT))
        // 월요일~일요일

        return "[${monday}~${sunday}] $username 주간 회고"
    }

    fun createLabel(zdt: ZonedDateTime): String = "${zdt.year}년_${zdt.weekOfYear()}주차"

}