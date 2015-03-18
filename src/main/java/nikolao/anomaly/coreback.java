/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nikolao.anomaly;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nikolao {Ioannis Nikolaou AM 4504 mailto:Gianhspc@gmail.com}
 * 15/3/2015 Created{First write}
 */
public class coreback {
   private final long id;
    private final String str1;
    private final String str2;
    private final String str3;

    public coreback(long id, String str1,String str2,String str3) {
        this.id = id;
        this.str1 = str1;
        this.str2 = str2;
        this.str3=str3;
    }

    public long getId() {
        return id;
    }

    public List<String> getContent() {
        List<String> content= new ArrayList();
        content.add(str1);
        content.add(str2);
        return content;
    } 
}
