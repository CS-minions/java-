public class ModelAPIFactory {

    public static ModelAPI createAPI(String vendor) {
        switch (vendor.toLowerCase()) {
            case "阿里-通义千问":
                return new AliyunAPI();
            case "百度-文心一言":
                return new BaiduAPI();
            default:
                throw new IllegalArgumentException("Unknown vendor: " + vendor);
        }
    }
}