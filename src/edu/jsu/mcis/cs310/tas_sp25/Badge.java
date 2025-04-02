package edu.jsu.mcis.cs310.tas_sp25;

import java.util.zip.CRC32;

public class Badge {
    
    // Instance fields (assuming your Badge has at least an id and description)
    private String id;
    private String description;
    
    // Existing constructor(s)
    public Badge(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    // New constructor for creating a new badge from a description only.
    // It computes the badge id as an 8-digit uppercase hexadecimal CRC-32 of the description.
    public Badge(String description) {
        this.description = description;
        CRC32 crc = new CRC32();
        crc.update(description.getBytes());
        long checksum = crc.getValue();
        // Format to exactly 8 uppercase hexadecimal digits (with leading zeros if necessary)
        this.id = String.format("%08X", checksum);
    }
    
    // Getters
    public String getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    // Setter for description (if updates are allowed)
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        // For example, return a string in the format: "#<id> (<description>)"
        return "#" + id + " (" + description + ")";
    }
}
