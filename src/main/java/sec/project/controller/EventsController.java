/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sec.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Event;

/**
 *
 * @author anho
 */
@Controller
public class EventsController {
    
    @Autowired
    private JdbcTemplate database;
    
    @RequestMapping("events")
    public String Events(Model model, @RequestParam(required = false) String query) {
        if (query != null && query != "") {
            // Security flaw #2: SQL injection possible due to not using parameterized query.
            String sql = "SELECT * FROM Event WHERE name = '" + query + "'";
            List<Event> events = new ArrayList<Event>();
            List<Map<String, Object>> rows = database.queryForList(sql);
            for (Map<String, Object> row : rows) {
                Event event = new Event();
                event.setName((String)row.get("name"));
                events.add(event);
            }
            model.addAttribute("events", events);
        }
        return "events";
    }
}
