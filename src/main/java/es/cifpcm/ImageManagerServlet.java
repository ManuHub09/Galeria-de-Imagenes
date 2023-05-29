package es.cifpcm;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/imageManager")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)   // 50MB

public class ImageManagerServlet extends HttpServlet {
    private static final String SAVE_DIR= "uploadsRodriguez";
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*String appPath = request.getServletContext().getRealPath("");
        String savePath = appPath + File.separator + SAVE_DIR;
        File fileDir = new File(savePath);

         */
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/uploadsRodriguez");
        File fileDir = new File(fullPath);
        File [] paths;
        PrintWriter wrt;
        wrt = response.getWriter();
        try {
            paths = fileDir.listFiles();
            if(paths != null){
                /*
                for (File path : paths) {
                    wrt.println("<a href="+SAVE_DIR+"/"+path.getName()+">imagen </a>");
                }
                 */
                wrt.println("<h1> Listas de imagenes:  </h1>");
                for (int i = 0; i <= paths.length-1; i++){
                    wrt.println("<div>");
                    wrt.println("<a href="+SAVE_DIR+"/"+paths[i].getName()+">imagen "+ (i+1)+" </a>");
                    wrt.println("</div>");
                }
            }
            else
            {
                wrt.println("<h1> No hay archivos </h1>");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        wrt.println("<a href='index.jsp'> Volver al inicio </a>");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String fullPath = context.getRealPath("/uploadsRodriguez");
        File fileDir = new File(fullPath);
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        for (Part part : request.getParts()){
            String fileName = extractFileName(part);
            fileName = new File(fileName).getName();
            part.write(fullPath + File.separator + fileName);
        }
        PrintWriter wrt;
        wrt = response.getWriter();
        wrt.println("<h1> Se ha subido con exito </h1>");
        wrt.println("<a href='index.jsp'> Volver al inicio </a>");
    }
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
}
