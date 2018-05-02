package JDM;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

public class JDM_TO_DB {


    private static int read_stats(BufferedReader in,
                                  int relation_stats_begin_idx, int relation_stats_end_idx,
                                  int node_stats_begin_idx, int node_stats_end_idx, int nb_line_to_read,
                                  int node_begin_idx, int node_end_idx,
                                  int relation_begin_idx,int relation_end_idx){
        int line_read_count=1;
        String line;

        JDM_DB jdm_db=new JDM_DB("","","");
        try{
            while ((line_read_count < nb_line_to_read) && ((line = in.readLine() )!= null)){

                if(line_read_count>= relation_begin_idx && line_read_count <= relation_end_idx){
                    String[] line_parts=line.split("\\|");
                    int rid = Integer.parseInt(line_parts[0].split("=")[1]);
                    int x=Integer.parseInt(line_parts[1].split("=")[1]);
                    int y=Integer.parseInt(line_parts[2].split("=")[1]);
                    int t=Integer.parseInt(line_parts[3].split("=")[1]);
                    int w=Integer.parseInt(line_parts[4].split("=")[1]);
                    jdm_db.insert_relation(rid,x,y,t,w);
                    //System.out.println(rid+","+x+","+y+","+t+","+w);
                }
                else if(line_read_count>= node_begin_idx && line_read_count<= node_end_idx){

                    String[] line_parts=line.split("\\|");

                    int eid = Integer.parseInt(line_parts[0].split("=")[1]);
                    String[] name_parts=line_parts[1].split("=\"");
                    String name=null;
                    if(name_parts.length > 1){
                       name=name_parts[1].split("\"")[0];
                    }

                    int node_type=Integer.parseInt(line_parts[2].split("=")[1]);
                    int node_weight=Integer.parseInt(line_parts[3].split("=")[1]);
                    jdm_db.insert_node(eid,name,node_type,node_weight);
                    //System.out.println(eid+","+name+","+t+","+w);
                }

                else if(line_read_count>= node_stats_begin_idx && line_read_count<= node_stats_end_idx){
                    String[] line_parts=line.split(" ");
                    String node_name=line_parts[5];
                    int node_type=Integer.parseInt(line_parts[6].split("=")[1].split("\\)")[0]);
                    //System.out.println("node : "+node_name + "," + node_type);
                    jdm_db.insert_node_type(node_type,node_name);
                }
                else if(line_read_count >= relation_stats_begin_idx && line_read_count<= relation_stats_end_idx){
                    String[] line_parts=line.split("\\|");

                    int r_id= Integer.parseInt(line_parts[0].split("=")[1]);
                    String r_name=line_parts[1].split("=\"")[1];
                    String r_nom_etendu=line_parts[2].split("=\"")[1];
                    String r_info=line_parts[3].split("=\"")[1];
                    jdm_db.insert_relation_type(r_id,r_name,r_nom_etendu,r_info);
                   // System.out.println("relation : "+r_id+","+r_name+","+r_nom_etendu+","+r_info);
                }

                if(line_read_count % 1000000 == 0){
                    System.out.print(line_read_count/1000000+",");
                }
                line_read_count++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return line_read_count;

    }

    public static void main(String[] args){
        if(args.length != 10){
            System.err.println("Bad usage : jdm_db_path nb_line_to_read");
            return;
        }
        String path=args[0];
        int nb_line_to_read=Integer.parseInt(args[1]);

        int relation_stats_begin_idx=Integer.parseInt(args[2]); //304
        int relation_stats_end_idx=Integer.parseInt(args[3]); //438

        int node_stats_begin_idx=Integer.parseInt(args[4]); // 221
        int node_stats_end_idx=Integer.parseInt(args[5]); //241

        int node_begin_idx=Integer.parseInt(args[6]); // 442
        int node_end_idx=Integer.parseInt(args[7]); // 9458242

        int relation_begin_idx=Integer.parseInt(args[8]); // 9458247
        int relation_end_idx=Integer.parseInt(args[9]); //189572088

        System.out.println("Start reading");
        try{
            long tstart=System.currentTimeMillis();

            String line;

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(path), "ISO-8859-1"));
            int line_read_count=
                    read_stats(in,
                            relation_stats_begin_idx,relation_stats_end_idx,
                            node_stats_begin_idx,node_stats_end_idx,nb_line_to_read,
                            node_begin_idx,node_end_idx,
                            relation_begin_idx,relation_end_idx);

            System.out.println("\nNbLine : "+line_read_count + "\ntime : "+(double)(System.currentTimeMillis()-tstart) + "ms");
        }
        catch (IOException e){
            e.printStackTrace();
        }


    }
}

