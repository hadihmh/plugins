import com.google.android.gms.maps.model.UrlTileProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
public class MapirUrlTileProvider extends UrlTileProvider {
    String OSGEO_WMS = "https://map.ir/shiveh?" +
            "service=WMS" +
            "&version=1.1.0" +
            "&EXCEPTIONS=application/vnd.ogc.se_inimage" +
            "&request=GetMap" +
            "&layers=Shiveh:Shiveh" +
            "&width=256" +
            "&height=256" +
            "&srs=EPSG:3857" +
            "&format=image/png" +
            "&bbox=%f,%f,%f,%f&x-api-key=";
    private String apiKey;
    private int MINX = 0;
    private int MAXX = 1;
    private int MINY = 2;
    private int MAXY = 3;
    private double[] TILE_ORIGIN = new double[]{-20037508.34789244, 20037508.34789244};
    MapirUrlTileProvider(String apiKey) {
        super(256, 256);
        this.apiKey = apiKey;
    }
    public URL getTileUrl(int x, int y, int z) {
        Double[] bbox = getBoundingBox(x, y, z);
        String s = String.format(Locale.US, OSGEO_WMS, bbox[MINX], bbox[MINY], bbox[MAXX], bbox[MAXY]) + apiKey;
        URL url;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
        return url;
    }
    private Double[] getBoundingBox(int x, int y, int zoom) {
        double tileSize = (20037508.34789244 * 2) / Math.pow(2.0, (double) zoom);
        Double[] bbox = new Double[4];
        bbox[MINX] = TILE_ORIGIN[0] + x * tileSize;
        bbox[MINY] = TILE_ORIGIN[1] - (y + 1) * tileSize;
        bbox[MAXX] = TILE_ORIGIN[0] + (x + 1) * tileSize;
        bbox[MAXY] = TILE_ORIGIN[1] - y * tileSize;
        return bbox;
    }
}