package net.lax1dude.eaglercraft.v2_6.internal.teavm.opts;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

/**
 * WebGL2 context attributes configuration JSO interface.
 * Maps to WebGLContextAttributes used when creating the WebGL2 context.
 */
public interface IClientConfigGL extends JSObject {

	/** Whether the drawing buffer has an alpha channel. Default: false. */
	@JSProperty
	boolean isAlpha();

	@JSProperty
	void setAlpha(boolean value);

	/** Whether the drawing buffer has a depth buffer. Default: true. */
	@JSProperty
	boolean isDepth();

	@JSProperty
	void setDepth(boolean value);

	/** Whether the drawing buffer has a stencil buffer. Default: false. */
	@JSProperty
	boolean isStencil();

	@JSProperty
	void setStencil(boolean value);

	/** Whether anti-aliasing is enabled. Default: false. */
	@JSProperty
	boolean isAntialias();

	@JSProperty
	void setAntialias(boolean value);

	/** Whether the drawing buffer contains pre-multiplied alpha. Default: false. */
	@JSProperty
	boolean isPremultipliedAlpha();

	@JSProperty
	void setPremultipliedAlpha(boolean value);

	/** Whether to preserve the drawing buffer after compositing. Default: false. */
	@JSProperty
	boolean isPreserveDrawingBuffer();

	@JSProperty
	void setPreserveDrawingBuffer(boolean value);

	/** Whether the drawing buffer has a stencil buffer (alias). Default: false. */
	@JSProperty
	boolean isStencilBuffer();

	@JSProperty
	void setStencilBuffer(boolean value);

	/** The power preference for the GPU. "default", "low-power", or "high-performance". */
	@JSProperty
	String getPowerPreference();

	@JSProperty
	void setPowerPreference(String value);

	/** Whether the context is created in fail-if-major-performance-caveat mode. Default: false. */
	@JSProperty
	boolean isFailIfMajorPerformanceCaveat();

	@JSProperty
	void setFailIfMajorPerformanceCaveat(boolean value);

	/** Whether the context supports desynchronized rendering. Default: false. */
	@JSProperty
	boolean isDesynchronized();

	@JSProperty
	void setDesynchronized(boolean value);
}
