package semi.dao;

import static utils.ConnectionUtil.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import semi.dto.CartItemDto;

public class CartDao {
	
	private static CartDao self = new CartDao();
	private CartDao() {}
	public static CartDao getInstance() {
		return self;
	}
	
	public List<CartItemDto> getCartItemList(int userNo) throws SQLException {
		List<CartItemDto> items = new ArrayList<CartItemDto>();
		String sql = "select c.user_no, c.product_item_no, c.cart_product_quantity, "
				+ "        i.product_size, i.product_color, i.product_no, "
				+ "        p.product_name, p.product_price, p.product_discount_price, "
				+ "		   p.product_discount_from, p.product_discount_to, p.product_on_sale, "
				+ "        t.thumbnail_image_url "
				+ "from semi_cart_item c, semi_product_item i, semi_product p, semi_product_thumbnail_image t "
				+ "where user_no = ? "
				+ "and t.thumbnail_image_url=p.product_no || '_1.jpg' "
				+ "and c.product_item_no = i.product_item_no "
				+ "and i.product_no = p.product_no "
				+ "and p.product_no = t.product_no ";
		Connection connection = getConnection();
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, userNo);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			CartItemDto item = new CartItemDto();
			item.setUserNo(rs.getInt("user_no"));
			item.setQuantity(rs.getInt("cart_product_quantity"));
			item.setProductItemNo(rs.getInt("product_item_no"));
			item.setSize(rs.getString("product_size"));
			item.setColor(rs.getString("product_color"));
			item.setProductNo(rs.getInt("product_no"));
			item.setName(rs.getString("product_name"));
			item.setPrice(rs.getLong("product_price"));
			item.setDiscountPrice(rs.getLong("product_discount_price"));
			item.setDiscountFrom(rs.getDate("product_discount_from"));
			item.setDiscountTo(rs.getDate("product_discount_to"));
			item.setOnSale(rs.getString("product_on_sale"));
			item.setThumbnailUrl(rs.getString("thumbnail_image_url"));
			items.add(item);
		}
		rs.close();
		pstmt.close();
		connection.close();
		return items;
	}
}