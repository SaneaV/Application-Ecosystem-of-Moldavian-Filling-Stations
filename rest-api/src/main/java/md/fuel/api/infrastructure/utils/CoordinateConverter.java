package md.fuel.api.infrastructure.utils;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;
import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;
import org.locationtech.proj4j.util.Pair;

@NoArgsConstructor(access = PRIVATE)
public class CoordinateConverter {

  private static final String WGS84_EPSG = "epsg:3857";
  private static final String UTM_EPSG = "epsg:4326";

  private static final CRSFactory CRS_FACTORY;
  private static final CoordinateReferenceSystem WGS84;
  private static final CoordinateReferenceSystem UTM;
  private static final CoordinateTransformFactory COORDINATE_TRANSFORM_FACTORY;
  private static final CoordinateTransform COORDINATE_TRANSFORM;

  static {
    CRS_FACTORY = new CRSFactory();
    WGS84 = CRS_FACTORY.createFromName(WGS84_EPSG);
    UTM = CRS_FACTORY.createFromName(UTM_EPSG);
    COORDINATE_TRANSFORM_FACTORY = new CoordinateTransformFactory();
    COORDINATE_TRANSFORM = COORDINATE_TRANSFORM_FACTORY.createTransform(WGS84, UTM);
  }

  public static Pair<Double, Double> convertWgs84ToUtm(double x, double y) {
    final ProjCoordinate result = new ProjCoordinate();
    COORDINATE_TRANSFORM.transform(new ProjCoordinate(x, y), result);
    return new Pair<>(result.x, result.y);
  }
}