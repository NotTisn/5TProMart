package com.fivetpromart.infrastructure.notification.email.adapter;

import com.fivetpromart.application.port.out.IEmailProviderPort;
import com.fivetpromart.infrastructure.notification.email.client.BrevoClient;
import com.fivetpromart.infrastructure.notification.email.dto.EmailDto;
import com.fivetpromart.infrastructure.notification.email.dto.RecipientDto;
import com.fivetpromart.infrastructure.notification.email.dto.SenderDto;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BrevoEmailAdapter implements IEmailProviderPort { // Triển khai Port "sạch"

    private final BrevoClient brevoClient; // Dùng Feign client "bẩn"

    @Value("${notification.email.brevo-apikey}")
    private String apiKey;

    @Value("${notification.email.sender-email}")
    @NonFinal
    private String senderEmail;

    @Value("${notification.email.sender-name}")
    private String senderName;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        // ADD THIS DEBUG LOGGING
        System.out.println("=== DEBUG: Sender Email = " + senderEmail);
        System.out.println("=== DEBUG: Sender Name = " + senderName);

        SenderDto sender = new SenderDto(senderName, senderEmail);
        RecipientDto recipient = new RecipientDto(toEmail, toEmail);

        String htmlContent = buildOtpTemplate(toEmail, otp);

        EmailDto brevoRequest = EmailDto.builder()
                .sender(sender)
                .to(List.of(recipient))
                .subject("Your 5TProMart OTP Code")
                .htmlContent(htmlContent)
                .build();

        // ADD THIS DEBUG LOGGING
        System.out.println("=== DEBUG: Request = " + brevoRequest);

        try {
            brevoClient.sendEmail(apiKey, brevoRequest);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Brevo", e);
        }
    }

    /**
     * (THÊM MỚI)
     * Hàm helper riêng để tạo mẫu HTML chuyên nghiệp cho email OTP.
     * Sử dụng CSS inline để tương thích tối đa.
     */
    private String buildOtpTemplate(String userName, String otp) {
        // Sử dụng Java Text Blocks (Java 15+) để viết HTML rõ ràng
        String template = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Your OTP Code</title>
            </head>
            <body style="font-family: Arial, 'Helvetica Neue', Helvetica, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4;">
                <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f4f4;">
                    <tr>
                        <td align="center">
                            <!-- Main Container -->
                            <table width="600" border="0" cellspacing="0" cellpadding="0" style="width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;">
                                
                                <!-- Header -->
                                <tr>
                                    <td align="center" style="padding: 20px; background-color: #f9f9f9; border-bottom: 1px solid #e0e0e0;">
                                        <h2 style="margin: 0; color: #333; font-size: 24px;">5TProMart</h2>
                                        <!-- Nếu bạn có logo: <img src="URL_LOGO" alt="5TProMart Logo" width="150"> -->
                                    </td>
                                </tr>
                                
                                <!-- Body Content -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <h1 style="margin: 0 0 20px 0; color: #333; font-size: 28px;">Your OTP Code</h1>
                                        <p style="margin: 0 0 20px 0; font-size: 16px; line-height: 1.5; color: #555;">
                                            Hello %s,
                                        </p>
                                        <p style="margin: 0 0 20px 0; font-size: 16px; line-height: 1.5; color: #555;">
                                            Please use the following One-Time Password (OTP) to complete your registration. This code is valid for 10 minutes.
                                        </p>
                                        
                                        <!-- OTP Code Box -->
                                        <table width="100%%" border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td align="center" style="padding: 20px; background-color: #f1f1f1; border-radius: 5px;">
                                                    <span style="font-size: 32px; font-weight: bold; color: #222; letter-spacing: 8px;">
                                                        %s
                                                    </span>
                                                </td>
                                            </tr>
                                        </table>
                                        
                                        <p style="margin: 20px 0 0 0; font-size: 16px; line-height: 1.5; color: #555;">
                                            If you did not request this, please ignore this email or contact support.
                                        </p>
                                    </td>
                                </tr>
                                
                                <!-- Footer -->
                                <tr>
                                    <td align="center" style="padding: 20px 30px; background-color: #f9f9f9; border-top: 1px solid #e0e0e0;">
                                        <p style="margin: 0; font-size: 12px; color: #888;">
                                            &copy; 2025 5TProMart. All rights reserved.
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """;

        // Thay thế các placeholder (%s) bằng giá trị thực tế
        // Chúng ta dùng userName (là email) để chào hỏi
        return String.format(template, userName, otp);
    }
}