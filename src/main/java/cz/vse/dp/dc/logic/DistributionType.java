package cz.vse.dp.dc.logic;

public enum DistributionType {
    NORMAL("normální"),
    UNIFORM("rovnoměrné");

    private final String type;

    DistributionType(String type) {
        this.type = type;
    }

    public static DistributionType findByValue(String value) {
        for (DistributionType dt : values()) {
            if (dt.type.equals(value)) {
                return dt;
            }
        }
        return null;
    }
}
