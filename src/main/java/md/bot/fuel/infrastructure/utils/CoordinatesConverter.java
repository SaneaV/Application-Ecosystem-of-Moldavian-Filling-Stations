package md.bot.fuel.infrastructure.utils;

import lombok.NoArgsConstructor;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.locationtech.proj4j.util.Pair;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CoordinatesConverter {

    private static final String WGS84_EPSG = "epsg:3857";
    private static final String UTM_EPSG = "epsg:4326";

    private final static CRSFactory crsFactory;
    private final static CoordinateReferenceSystem WGS84;
    private final static CoordinateReferenceSystem UTM;
    private final static CoordinateTransformFactory coordinateTransformFactory;
    private final static CoordinateTransform coordinateTransform;

    static {
        crsFactory = new CRSFactory();
        WGS84 = crsFactory.createFromName(WGS84_EPSG);
        UTM = crsFactory.createFromName(UTM_EPSG);
        coordinateTransformFactory = new CoordinateTransformFactory();
        coordinateTransform = coordinateTransformFactory.createTransform(WGS84, UTM);
    }

    public static Pair<Double, Double> convertWGS84ToUTM(double x, double y) {
        final ProjCoordinate result = new ProjCoordinate();
        coordinateTransform.transform(new ProjCoordinate(x, y), result);
        return new Pair<>(result.x, result.y);
    }
}