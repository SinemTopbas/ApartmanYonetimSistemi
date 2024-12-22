public class Property {
    private String propertyId;
    private boolean isOccupied;
    private String tenantStatus;
    private String propertyType;

    public Property(String propertyId, boolean isOccupied, String tenantStatus, String propertyType) {
        this.propertyId = propertyId;
        this.isOccupied = isOccupied;
        this.tenantStatus = tenantStatus;
        this.propertyType = propertyType;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public String getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(String tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public String toString() {
        return "Property ID: " + propertyId + " | " + (isOccupied ? "Occupied (" + tenantStatus + ")" : "Empty (" + propertyType + ")");
    }
}
