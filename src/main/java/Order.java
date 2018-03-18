import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@WebServlet("/order")
public class Order extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        StringBuffer jb = new StringBuffer();
        String line = null;
        String container = "<b>Спасибо за заказ!</b></br></br></br>";

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

        JSONObject jsonObject = new JSONObject(jb.toString());

        JSONArray array = (JSONArray) jsonObject.get("cartItems");

        for (int i = 0; i < array.length(); i++){
            JSONObject obj = (JSONObject) array.get(i);

            String itemName = obj.getString("name");
            String itemPrice = obj.getString("currentPrice");
            String itemDescription = obj.getString("description");

            container += (i+1 + ". " + itemName + " - " + itemPrice + " BYN<br>");
            container += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + itemDescription + "<br><br>";
        }

        String name = (String) jsonObject.get("name");
        String phone = (String) jsonObject.get("phone");
        String address = (String) jsonObject.get("address");
        String mess = (String) jsonObject.get("message");
        String pay =(String) jsonObject.get("pay");
        String email = (String) jsonObject.get("email");

        container += "</br></br></br></br></br>";
        container += "Телефон: " + phone + "</br>Адрес: " + address + "</br>Оплата: " + pay + "</br>Сообщение: " + mess;

        String from = "karpovichtest@gmail.com";
        final String username = "karpovichtest";
        final String password = "karpovich123";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Get the Session object.
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress("karpovichtest@gmail.com"));
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(from));
            // Set Subject: header field
            message.setSubject("Заказ от " + name + " (" + email + ")");
            // Now set the actual message
            message.setContent(container, "text/html; charset=utf-8");
            // Send message
            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("/#/shopping-cart");
    }
}