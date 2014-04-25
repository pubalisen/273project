package edu.sjsu.cmpe.dropbox.domain;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import edu.sjsu.cmpe.dropbox.config.MongoConfig;

/**
 * @author Team Projections This class contains methods which will have common
 *         amazon functions which we need for every operation that we will
 *         perform on amazon S3.
 * 
 */
public class AmazonCredentials {
	/**
	 * @param credentials
	 * @return AmazonS3Client
	 */
	public AmazonS3Client getClient(AWSCredentials credentials) {
		AmazonS3Client client = new AmazonS3Client(credentials);
		client.setEndpoint("http://s3-us-west-1.amazonaws.com");
		return client;
	}

	/**
	 * @return AWSCredentials
	 */
	public AWSCredentials getCredentials() {
		AWSCredentials credentials = new BasicAWSCredentials(
				MongoConfig.getAmazonUsername(),
				MongoConfig.getAmazonPassword());
		return credentials;
	}

	/**
	 * @param bucketName
	 * @param client
	 * @return Bucket
	 */
	public Bucket createNewBucket(String bucketName,
			AmazonS3Client client) {
		CreateBucketRequest request = new CreateBucketRequest(bucketName);
		Bucket result = client.createBucket(request);
		if (result != null) {
			System.out.println("Created bucket successfully: "
					+ result.getName());
		} else {
			System.out.println("ERROR IN CREATING BUCKET ");
		}
		return result;
	}
}