package com.board.domain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;

import java.util.Date;
import java.util.Iterator;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDescriptor;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.Directory;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.GeoLocation;

import static com.drew.metadata.exif.GpsDirectory.*;

public class Files {

	private static final String SAVE_PATH = "C:\\Users\\skyhu\\Desktop\\fileUpload";
	private static final String PREFIX_URL = "/upload/";

	private List<MultipartFile> files = null;

	private List<String> filesGPS = null;
	private List<String> filesTime = null;

	public void setFiles(List<MultipartFile> filesList) throws Exception {
		this.files = filesList;
		multipartToFile(files);
	}

	private boolean writeFile(MultipartFile multipartFile, String saveFileName) throws Exception {
		boolean result = false;

		byte[] data = multipartFile.getBytes();
		FileOutputStream fos = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
		fos.write(data);
		fos.close();

		return result;
	}

	private void multipartToFile(List<MultipartFile> multipart) throws Exception {
		for (MultipartFile mF : multipart) {
			String saveFileName = mF.getOriginalFilename();
			writeFile(mF, saveFileName);
			File convFile = new File(saveFileName);
			mF.transferTo(convFile);
			fileMetadata(convFile);
		}
	}

	private void fileMetadata(File file) throws JpegProcessingException {
		try {

			Metadata metadata = JpegMetadataReader.readMetadata(file);
			GpsDirectory gpsDirectory = metadata.getDirectory(GpsDirectory.class);
			ExifSubIFDDirectory directory = metadata.getDirectory(ExifSubIFDDirectory.class);

			GeoLocation exifLocation = gpsDirectory.getGeoLocation();
			System.out.print(exifLocation.getLatitude());
			System.out.print(exifLocation.getLongitude());

			Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
			System.out.print(date);

		} catch (IOException ex) {

		}
	}

}
