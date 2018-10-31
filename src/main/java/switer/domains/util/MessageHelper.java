package switer.domains.util;

import switer.domains.User;

public abstract class MessageHelper {
	public static String getAuthorName(User author) {
		return author != null ? author.getUsername() : "<none>";
	}
}