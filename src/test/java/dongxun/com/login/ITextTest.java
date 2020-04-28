package dongxun.com.login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.itextpdf.html2pdf.HtmlConverter;

public class ITextTest {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		StringBuilder sb = new StringBuilder();
		try(Stream<String> stream=Files.lines(Paths.get("D:/demo.html"))){
			stream.forEach(s->sb.append(s));
		}
		File tempFile=Files.createTempFile("abc-", ".pdf").toFile();
		System.out.println(tempFile.getPath());
		HtmlConverter.convertToPdf(sb.toString(), new FileOutputStream(tempFile));
	}

}
