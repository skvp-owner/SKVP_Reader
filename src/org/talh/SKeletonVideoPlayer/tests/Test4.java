package org.talh.SKeletonVideoPlayer.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.talh.SKeletonVideoPlayer.SKVPIllegalValueException;
import org.talh.SKeletonVideoPlayer.SKVPReader;
import org.talh.SKeletonVideoPlayer.SKVPSyntaxErrorException;

public class Test4 extends AbstractTest {

	protected int run() {
		ClassLoader cl = this.getClass().getClassLoader();
		URL resourceUrl = cl.getResource("org/talh/SKeletonVideoPlayer/tests/resources/file3.skvp");
		if (resourceUrl == null) {
			return 1;
		}
		String filePath = resourceUrl.getFile();
		try {
			new SKVPReader(new File(filePath));
		} catch (SKVPSyntaxErrorException e) {
			return 0;
		} catch (IOException e) {
			return 2;
		} catch (SKVPIllegalValueException e) {
			return 3;
		}
		return 4;		
	}
	
}
