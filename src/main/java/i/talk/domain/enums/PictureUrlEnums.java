package i.talk.domain.enums;

public enum PictureUrlEnums {

SPIDEY("https://d2z1w4aiblvrwu.cloudfront.net/ad/d3dA/general-mills-spider-man-into-the-spider-verse-spidey-outfit-large-4.jpg"),
POOP("http://cdn.shopify.com/s/files/1/1061/1924/products/Poop_Emoji_7b204f05-eec6-4496-91b1-351acc03d2c7_grande.png?v=1480481059"),
	MJ("https://img-s-msn-com.akamaized.net/tenant/amp/entityid/BBVBnmV.img?h=608&w=624&m=6&q=60&o=f&l=f&x=719&y=557"),
	SW("https://lumiere-a.akamaihd.net/v1/images/og-generic_02031d2b.png?region=0%2C0%2C1200%2C1200"),
	THANOS("https://cdn.vox-cdn.com/thumbor/es0br70k3C3mw4hf1fgItGCNkPw=/0x0:1280x705/1200x800/filters:focal(323x177:527x381)/cdn.vox-cdn.com/uploads/chorus_image/image/60325351/thanos.0.jpeg"),
	PUITN("https://belsat.eu/wp-content/uploads/2019/03/2019-03-14T113734Z_1010715600_RC1497A8D440_RTRMADP_3_RUSSIA-BUSINESS-P-660x440.jpg?1553379521"),
	TRUMP("https://www.seriousfacts.com/wp-content/uploads/2017/03/donald-trump-facts-funny-758x426.jpg");

	public final String label;


	private PictureUrlEnums(String label) {
		this.label = label;
	}

	public static PictureUrlEnums getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}

}
