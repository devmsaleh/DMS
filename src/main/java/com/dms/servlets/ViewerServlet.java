package com.dms.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.dms.dao.DocumentRepository;
import com.dms.entities.Document;

public class ViewerServlet extends HttpServlet {

    private DocumentRepository documentRepository;

    public ViewerServlet(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("########## ViewerServlet doGet,id: " + request.getParameter("id"));

        OutputStream out = null;
        InputStream inputStream = null;
        try {

            String id = request.getParameter("id");
            String mode = request.getParameter("mode");
            String fileName = null;
            String contentType = null;
            int contentLength = 0;

            if (StringUtils.isNotBlank(id)) {
                // id = Encryptor.decrypt(id);

                Optional<Document> documentOptional = documentRepository.findById(Long.parseLong(id));
                Document document = documentOptional.get();
                System.out.println("########## document: " + document);
                Path path = Paths.get(document.getFullPath());
                byte[] data = Files.readAllBytes(path);
                inputStream = new ByteArrayInputStream(data);
                contentLength = inputStream.available();

                fileName = document.getOriginalFileName();
                contentType = document.getMimeType();

            }

            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replaceAll("\\+", " ");

            response.setContentType(contentType + ";charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            if (mode != null && mode.equals("download")) {
                response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            } else {
                response.setHeader("Content-disposition", "inline; filename=" + fileName);
            }
            response.setContentLength(contentLength);
            response.setHeader("Content-Length", String.valueOf(contentLength));

            out = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
    }

}
