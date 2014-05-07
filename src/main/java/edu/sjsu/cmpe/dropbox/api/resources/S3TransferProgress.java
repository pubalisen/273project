package edu.sjsu.cmpe.dropbox.api.resources;

/*
 * Copyright 2010-2014 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import edu.sjsu.cmpe.dropbox.dto.MongoTest;

/**
 * Demonstrates how to upload data to Amazon S3, and track progress, using a
 * Swing progress bar.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on
 * Amazon S3, see http://aws.amazon.com/s3.
 * <p>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in the
 *                   AwsCredentials.properties file before you try to run this
 *                   sample.
 * http://aws.amazon.com/security-credentials
 */
public class S3TransferProgress {

    private static AWSCredentials credentials;
    private static TransferManager tx;
    private static String bucketName;

    private JProgressBar pb;
    private JFrame frame;
    private Upload upload;
    private JButton button;
	private MongoTest mongo;
    public static long size;
    public static String fileName;
    
    
    public S3TransferProgress(String Name, AmazonS3 s3Client, MongoTest mongo) {
        frame = new JFrame("Amazon S3 File Upload");
        button = new JButton("Choose File...");
        button.addActionListener(new ButtonListener());
        this.mongo = mongo;
        System.out.println("Mongo is" + mongo);
        System.out.println("this.Mongo is" + this.mongo);
        pb = new JProgressBar(0, 100);
        pb.setStringPainted(true);

        frame.setContentPane(createContentPane());
        frame.pack();
        frame.setVisible(true);
        
        s3Client.setEndpoint("http://s3-us-west-1.amazonaws.com");
        tx = new TransferManager(s3Client);
        bucketName = Name;
        System.out.print("Bucket is" +bucketName);
        System.out.print("Bucket1 is" +Name);
       
    }

    
	class ButtonListener implements ActionListener {    
   	
		public void actionPerformed(ActionEvent ae) {
            JFileChooser fileChooser = new JFileChooser();
            int showOpenDialog = fileChooser.showOpenDialog(frame);
            if (showOpenDialog != JFileChooser.APPROVE_OPTION) return;

           
            ProgressListener progressListener = new ProgressListener() {
                public void progressChanged(ProgressEvent progressEvent) {
                    if (upload == null) return;

                    pb.setValue((int)upload.getProgress().getPercentTransfered());

                    switch (progressEvent.getEventCode()) {
                    case ProgressEvent.COMPLETED_EVENT_CODE:
                        pb.setValue(100);
                        break;
                    case ProgressEvent.FAILED_EVENT_CODE:
                        try {
                            AmazonClientException e = upload.waitForException();
                            JOptionPane.showMessageDialog(frame,
                                    "Unable to upload file to Amazon S3: " + e.getMessage(),
                                    "Error Uploading File", JOptionPane.ERROR_MESSAGE);
                        } catch (InterruptedException e) {}
                        break;
                    }
                }
            };

            File fileToUpload = fileChooser.getSelectedFile();
            size = fileToUpload.length()/1048786;
            fileName = fileToUpload.getName();
            Boolean allow = mongo.checkFileSizeToUpload(bucketName,size);
            System.out.println(allow);
            System.out.println(size);
            if(allow == true){
            System.out.println("Mongo is" + mongo);
            mongo.addNewFileDetails(bucketName, fileName, "C:\\FakePath", size);
            
            PutObjectRequest request = new PutObjectRequest(
                    bucketName, fileToUpload.getName(), fileToUpload)
                .withProgressListener(progressListener);
           upload = tx.upload(request);
            
            /*size = fileToUpload.length()/1024;
            fileName = fileToUpload.getName();
            System.out.println("Mongo is" + mongo);
            mongo.addNewFileDetails(bucketName, fileName, "C:\\FakePath", size);*/
            setSize(size);
            System.out.print(size);
            setfileName(fileName);
            System.out.println(fileName);
            

        }
    }
	}

    public void setSize(long size){
    	this.size =size;
    	System.out.println(size);
    	System.out.println(this.size);
    	
    }

    public void setfileName(String fileName){
    	this.fileName =fileName;
    	System.out.println(fileName);
    	System.out.println(this.fileName);
    	
    }
    public long getfileSize(){
    		System.out.print(size);
    		return size;
    		}

    public String getfileName(){
    	System.out.println(fileName);
    	return fileName;
    	}
    
    


    private JPanel createContentPane() {
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(pb);

        JPanel borderPanel = new JPanel();
        borderPanel.setLayout(new BorderLayout());
        borderPanel.add(panel, BorderLayout.NORTH);
        borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return borderPanel;
    }
}
