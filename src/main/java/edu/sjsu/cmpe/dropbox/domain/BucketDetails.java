package edu.sjsu.cmpe.dropbox.domain;
import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
/*
public class BucketDetails {

    /**
     * @return the isbn
     
    public String getFile() {
	return file;
    }

    /**
     * @param isbn
     *            the isbn to set
     
    public void setFile(String file) {
	this.file = file;
    }

    /**
     * @return the title
     
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     
    public void setTitle(String title) {
	this.title = title;
    }
    
    /**
     * @return the publication date
     
    public String publishdate() {
	return publishdate;
    }

    /**
     * @param publishdate
     *            the publishdate to set
     
    public void setPublish(String publishdate) {
	this.publishdate = publishdate;
    }
    
    /**
     * @return the language
     
    public String language() {
	return language;
    }

    /**
     * @param language
     *            the language to set
     
    public void setlanguage(String language) {
	this.language = language;
    }
    
    
     * @return the number of pages
     
    public Long noOfPages() {
	return noOfPages;
    }

    /**
     * @param noOfPages
     *            the noOfPages to set
     
    public void noOfPages(Long noOfPages) {
	this.noOfPages = noOfPages;
    }
    
    /**
     * @return the status
     
    public String getStatus() {
	return status;
    }

    /**
     * @param status
     *            the status to set
     
    public void setStatus(String status) {
	this.status = status;
    }
    
    public void addAuthors(Author author)
	{
		authors.add(author);
	}
	
	public ArrayList<Author> getAuthors()
	{
		return authors;
	}
	
	public void setAuthors(ArrayList<Author> authors)
	{
		this.authors = authors;
	}
	
		
	public ArrayList<Review> getReviews()
	{
		return reviews;
	}
	
	public void setReviews(ArrayList<Review> reviews)
	{
		this.reviews = reviews;
	} 
	
	public void addReviews(Review review)
	{
		review.setId(reviewCount);
		reviews.add(review);
		reviewCount++;
		
	}
	
	public Review getReviewById(long id)
	{
		//System.out.println("size :" + reviews.size());
		for(int i = 1 ; i <= reviews.size() ; i++)
		{
			if(i == id)
				return reviews.get(i - 1);
		}
		return null;
	}
    
	public Author getAuthorById(long id)
	{
		//Author author = new Author();
		for(int i = 0 ; i <= authors.size() ; i++)
		{
			if(i == id)
				return authors.get(i - 1);
		}
		return null;
	}
    
}
*/
