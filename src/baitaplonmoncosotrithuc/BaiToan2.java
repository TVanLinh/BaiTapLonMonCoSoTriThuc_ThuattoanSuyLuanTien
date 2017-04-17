/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baitaplonmoncosotrithuc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * @author asus
 */
public class BaiToan2 {
     ArrayList<Luat> RULE=new  ArrayList<Luat>();
    ArrayList<String> GT=new  ArrayList<String>();
    ArrayList<String> KL =new  ArrayList<String>();
    public ArrayList<Luat> SAT=new  ArrayList<Luat>();
    ArrayList<String> TG =new  ArrayList<String>();
    String nodung="";
   
    String kp="";
    //docluat tu file
    public  int docLuat(String nameFile)
    {
       // nodung.append("Ok men");
        File file=new File(nameFile);
        if(!file.exists())
        {
            System.err.println("Sai rồi nhé");
            kp="File không tồn tại";
           // System.exit(0);
            return 0;
        }
      //   nodung.append("Co file");
        try
        {
            FileInputStream input=new FileInputStream(nameFile);
            InputStreamReader isR=new InputStreamReader(input);
            BufferedReader bfR=new BufferedReader(isR);
            String str="";
            int i=0;
            String []arr;
            while((str=bfR.readLine())!=null)
            {
                if(i==0)
                {
                     arr=str.split(",");
                     GT.addAll(Arrays.asList(arr));
                }else if(i==1)
                {
                     arr=str.split(",");
                     KL.addAll(Arrays.asList(arr));
                }else
                {
                    arr=str.split("->");
                    RULE.add(new Luat(arr[0],arr[1]));
                }
                i++;
            }
            bfR.close();
            isR.close();
            input.close();
            
        }catch(FileNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        return 1;
    }
  
   public void   thuatToanSuyDienTien(String fileName)
   {
       docLuat(fileName);
      
       String str= ("Tập giả thiết (GT) ban đầu là : " + GT + "\n");
       nodung=nodung+str;
       nodung += ("Tập kết luận (KL) là: " + KL + "\n");
       nodung += "Tập luật RULE: \n";
      
        for(Luat luat:RULE)
        {
              // System.out.println(luat.toString());
               nodung+=luat.toString()+"\n";
        }
        Luat r=new Luat();
        TG=GT;//gan TG =GT
        loc();//lay tap SAT tuong ung voi buoc SAT=loc(RULE,TG) trong giai thuat
        
        int i=0;
        while(kt_KL_Thuoc_TG()==false&& SAT.size()!=0)
        {
            nodung+="=======================Buoc "+(i+1)+"===================================================\n";
            nodung+="Tập SAT : \n";
            for(Luat luat:SAT)
            {
               nodung+=luat.toString()+" ;  ";
            }
            nodung+="\n";
            r=SAT.get(0);// lay mot  luat trong SAT
            nodung+="Luật r (sử dụng): "+r.toString()+"\n";
            if(!kiemTraTrung(r.vp))//kiem tra xem vp cua r da thuoc TG chua neu chua thi them vp vao tg
            {
                 TG.add(r.vp);//tuong ung voi buoc TG->TG V vp cua r
            }
            nodung+="Tập trung gian(TG) ở bước "+(i+1)+ ": "+TG+"\n";
            loatLuatKhoiRULE(r);//tuong ung voi buoc RULE=RULE\{r} trong giai thuat
            loc();//lay tap SAT tuong ung voi buoc SAT=loc(RULE,TG) trong giai thuat
            nodung+="Tập luật RULE: \n";
            for(Luat luat:RULE)
            {
               nodung+=luat.toString()+" ;  ";
            }
             nodung+="\n";
            i++;
        }
       
        System.out.println("======================================");
        if(kt_KL_Thuoc_TG())
        {
            // System.out.println("Thanh cong: Tap ket luan: "+KL+"   Tap gia thiet: "+TG);
            kp="Thành công: Tập kết luận(KL): "+KL+"   Tập giả thiết(GT): "+TG;
            JOptionPane.showMessageDialog(null,"Thành công.!");
        }
        else
        {
          
             kp=  "Không thành công : Tập kết luận (KL): "+KL+"   Tập giả thiết(GT): "+TG;
             JOptionPane.showMessageDialog(null,"Không thành công.!");
        }
         
    }
   
    public void loatLuatKhoiRULE(Luat r)
    {
        int dem=-1;
        for(int i=0;i<RULE.size();i++)
        if(r.vp.equals(RULE.get(i).vp) && r.vt.equals(RULE.get(i).vt))
        {
            dem=i;
            break;
        }
        if(dem!=-1)
        {
            RULE.remove(dem);
        }
    }
    //---------lay tap SAT
   public void loc()
  {
       SAT.clear();
        for(int j=0;j<RULE.size();j++)
        {
               if(kiemtraTHVa(RULE.get(j).vt))
               {
                    SAT.add(RULE.get(j));
                }
        }
   }
   //------------------------------------
    // vi du neu a va b da thuoc Tg thi a.b cung phai thuoc tg
    public boolean kiemtraTHVa(String str)
    {
       String arr[]=str.split(",");
       int dem=0;
       for(int i=0;i<arr.length;i++)
       {
           for(int j=0;j<TG.size();j++)
           {
               if(arr[i].equals(TG.get(j)))
               {
                   dem++;
               }
           }
       }
       if(dem==arr.length)
            return true;
       return false;
    }
   //kiem tra xem trong TG da co str chua neu co thi tra ve true
   public boolean kiemTraTrung(String str)
   {
       for(int i=0;i<TG.size();i++)
       {
           if(TG.get(i).equals(str))
           {
               return true;
           }
       }
       return false;
   }
   
   //--Kiem tra xem KL co thuoc TG khong
   public boolean kt_KL_Thuoc_TG()
   {
       //neu dem bang do dai cua tap ket luan thi KL thuoc TG
       int dem=0;
       for(int i=0;i<KL.size();i++)
       {
           for(int j=0;j<TG.size();j++)
           {
               if(KL.get(i).equals(TG.get(j)))
               {
                   dem++;
               }
           }
       }
       if(dem==KL.size())
            return true;
       return false;
   }
}
