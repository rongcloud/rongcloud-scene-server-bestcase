package cn.rongcloud.mic.common.jobs;

import cn.rongcloud.mic.community.service.TMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class DeleteMessageJob {

    @Autowired
    private TMessageService tMessageService;

    @Scheduled(cron = "0 0 12 * * ?")
    public void deleteMessage() {
        log.info("delete message job start");
        Date date = DateUtils.addDays(new Date(), -15);
        tMessageService.deleteMessageByDate(date);
        log.info("delete message job end");
    }
}
