package hu.skawa.migrator.model;

public class Dependency {
	private String groupId;
	private String artifactId;
	private String version;
	
	public Dependency() {
	}
	
	public Dependency(String groupId, String artifactId, String version) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}
	
	public String getArtifactId() {
		return artifactId;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public String getVersion() {
		return version;
	}
	
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Group: ");
		sb.append(this.groupId);
		sb.append("\n");
		sb.append("Artifact: ");
		sb.append(this.artifactId);
		sb.append("\n");
		sb.append("Version: ");
		sb.append(this.version);
		sb.append("\n");
		return sb.toString();
	}
}
