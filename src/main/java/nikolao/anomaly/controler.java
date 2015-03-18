/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nikolao.anomaly;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import static nikolao.anomaly.util.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import us.jubat.anomaly.AnomalyClient;

/**
 *
 * @author nikolao {Ioannis Nikolaou AM 4504 mailto:Gianhspc@gmail.com}
 * 15/3/2015 Created{First write}
 */
@RestController
public class controler {
    private static final String content1 = "normalScore:, %f!";
    private static final String content2 = "abnormalScore:, %f!";
    private final AtomicLong counter = new AtomicLong();//RequestParam(type="type",defaultValue="temperature") String type

    @RequestMapping("/anomaly")
    public coreback anomaly(@RequestParam(value="value", defaultValue="0") double value,@RequestParam(value="type",defaultValue="Noise") String type)throws UnknownHostException {
        List<Float> scores=new ArrayList();
        scores=getScore(value,type);
        return new coreback(counter.incrementAndGet(),String.format(content1, scores.get(0)),String.format(content2, scores.get(1)),type);
    }    
    
}
