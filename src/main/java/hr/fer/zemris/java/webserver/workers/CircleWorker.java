package hr.fer.zemris.java.webserver.workers;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;
/**
 * Implementation of {@link IWebWorker} interface. It draws
 * a red circle to client.
 * 
 * @author Vilim Staroveški
 *
 */
public class CircleWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) {
		final int width = 200;
		final int height = 200;
		context.setMimeType("image/png");
		BufferedImage bim = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = bim.createGraphics();
		
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		
		g2d.setColor(Color.RED);
		g2d.fillOval(0, 0, width, height);
		g2d.dispose();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			System.err.println("There was an IO error occured while writing to client.");
			System.err.println(e.getMessage());
		}
		try {
			context.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
