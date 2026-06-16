package net.lax1dude.eaglercraft.v2_6.internal.teavm.opts;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

/**
 * Server entry configuration JSO interface.
 * Represents a single server in the server list.
 */
public interface IClientConfigServer extends JSObject {

	/** Gets the display name of the server. */
	@JSProperty
	String getName();

	@JSProperty
	void setName(String value);

	/** Gets the server address (WebSocket URI). */
	@JSProperty
	String getAddress();

	@JSProperty
	void setAddress(String value);

	/** Gets whether to hide the server from the list. */
	@JSProperty
	boolean isHide();

	@JSProperty
	void setHide(boolean value);

	/** Gets whether to require password authentication. */
	@JSProperty
	boolean isPasswordRequired();

	@JSProperty
	void setPasswordRequired(boolean value);

	/** Gets the server MOTD (message of the day) override. */
	@JSProperty
	String getMotd();

	@JSProperty
	void setMotd(String value);
}
