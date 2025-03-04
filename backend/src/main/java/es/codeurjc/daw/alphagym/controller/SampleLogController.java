package es.codeurjc.daw.alphagym.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ch.qos.logback.classic.Logger;

@Controller
public class SampleLogController {
 private Logger log = (Logger) LoggerFactory.getLogger(SampleLogController.class);
 @GetMapping("/page_log")
 public String page(Model model) {
 log.trace("A TRACE Message");
 log.debug("A DEBUG Message");
 log.info("An INFO Message");
 log.warn("A WARN Message");
 log.error("An ERROR Message");
 return "page";
 }
}

