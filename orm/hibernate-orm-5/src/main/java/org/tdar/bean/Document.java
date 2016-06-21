package org.tdar.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;

/**
 * Represents a Document information resource.
 * 
 * The design decision was made to have null fields instead of overloading fields to mean different things for different
 * document types, e.g., a journal article has journal volume, journal name, and journal number instead of series name, series number,
 * and volume / # of volumes
 * 
 * NOTE: uses Resource.dateCreated as year published field
 * 
 * 
 * @author <a href='mailto:Allen.Lee@asu.edu'>Allen Lee</a>
 * @version $Rev$
 */
@Entity
@Table(name = "document")
@XmlRootElement(name = "document")
public class Document extends InformationResource {

    private static final long serialVersionUID = 7895887664126751989L;


    @Column(name = "series_name")
    @Length(max = 255)
    private String seriesName;

    @Column(name = "series_number")
    @Length(max = 255)
    private String seriesNumber;

    @Column(name = "number_of_pages")
    private Integer numberOfPages;

    @Length(max = 255)
    private String edition;

    @Length(max = 255)
    private String isbn;

    @Length(max = 255)
    @Column(name = "book_title")
    private String bookTitle;

    @Length(max = 255)
    private String issn;

    @Column(name = "start_page")
    @Length(max = 10)
    private String startPage;

    @Column(name = "end_page")
    @Length(max = 10)
    private String endPage;

    @Column(name = "journal_name")
    @Length(max = 255)
    private String journalName;

    @Length(max = 255)
    private String volume;

    @Column(name = "number_of_volumes")
    private Integer numberOfVolumes;

    @Column(name = "journal_number")
    @Length(max = 255)
    private String journalNumber;

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesNumber() {
        return seriesNumber;
    }

    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getNumberOfVolumes() {
        return numberOfVolumes;
    }

    public void setNumberOfVolumes(Integer numberOfVolumes) {
        this.numberOfVolumes = numberOfVolumes;
    }

    @Deprecated
    public Integer getNumberOfPages() {
        return numberOfPages;
    }


    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public String getEndPage() {
        return endPage;
    }

    public void setEndPage(String endPage) {
        this.endPage = endPage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public String getJournalNumber() {
        return journalNumber;
    }

    public void setJournalNumber(String journalNumber) {
        this.journalNumber = journalNumber;
    }
}
