import java.util.*;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;

public class Message {
    public static void main(String[] args) {
        Scanner k = new Scanner(System.in);
        OpenTelemetrySdk o = OpenTelemetrySdk.builder()
                .setTracerProvider(SdkTracerProvider.builder().build())
                .buildAndRegisterGlobal();

        Tracer t = o.getTracer("message tracer");
        System.out.print("Enter your name: ");
        String sender = k.nextLine();

        System.out.print("Enter recipient's name: ");
        String recipient = k.nextLine();

        System.out.print("Enter your message: ");
        String message = k.nextLine();

        Span span = t.spanBuilder("send message").startSpan();
        try (Scope sc = span.makeCurrent()) {
            span.setAttribute("sender", sender);
            span.setAttribute("recipient", recipient);
            span.setAttribute("message", message);
            System.out.println(sender + "sends a message to" + recipient + ":" + message);
            span = t.spanBuilder("receive message").startSpan();
            try (Scope sc2 = span.makeCurrent()) {
                span.setAttribute("sender", sender);
                span.setAttribute("recipient", recipient);
                span.setAttribute("message", message);

                System.out.println(recipient + " receiving a message from " + sender + ": " + message);
            } finally {
                span.end();
            }
        } finally {
            span.end();
        }
    }
}
