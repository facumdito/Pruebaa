package ar.com.rosario.realestate.api.pdf;

import ar.com.rosario.realestate.core.domain.Propiedad;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

@Service
public class FichaPdfService {

    private final TemplateEngine templateEngine;

    public FichaPdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generarFichaPropiedad(Propiedad propiedad) {
        Context ctx = new Context(Locale.forLanguageTag("es-AR"));
        ctx.setVariable("propiedad", propiedad);

        String html = templateEngine.process("ficha-propiedad", ctx);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(baos);
            builder.run();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando ficha PDF", e);
        }
    }
}
