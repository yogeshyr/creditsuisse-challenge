import hudson.*
import hudson.security.*
import jenkins.model.*
import java.util.*
import com.michelin.cio.hudson.plugins.rolestrategy.*
import com.synopsys.arc.jenkins.plugins.rolestrategy.*
import java.lang.reflect.*
import java.util.logging.*
import groovy.json.*

def env = System.getenv()

/**
 * ===================================
 *         
 *                Roles
 *
 * ===================================
 */

 def globalRoleAdmin = "admin"
 def globalRoleDev = "developer"
 def globalRoleDeployer = "deployer"
 def globalRolePdeployer = "prod-deployer"
 /**
 * ===================================
 *         
 *           Users and Groups
 *
 * ===================================
 */
def access = [
  admins: ["admin"],
  developers: [],
  deployers: [],
  proddeployers: []
]

/**
 * ===================================
 *         
 *           Permissions
 *
 * ===================================
 */

// TODO: drive these from a config file
def adminPermissions = [
"hudson.model.Hudson.Administer",
"hudson.model.Hudson.Read",
]

def developerPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Discover",
"hudson.model.Item.Read",
"hudson.model.Item.Configure",
"hudson.model.Item.Workspace",
"hudson.model.Item.Move"
]

def deployerPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read",
"hudson.model.Run.Replay"
]

def proddeployerPermissions = [
"hudson.model.Hudson.Read",
"hudson.model.Item.Build",
"hudson.model.Item.Cancel",
"hudson.model.Item.Read",
"hudson.model.Run.Replay"
]

def roleBasedAuthenticationStrategy = new RoleBasedAuthorizationStrategy()
Jenkins.instance.setAuthorizationStrategy(roleBasedAuthenticationStrategy)

Constructor[] constrs = Role.class.getConstructors();
for (Constructor<?> c : constrs) {
  c.setAccessible(true);
}

// Make the method assignRole accessible
Method assignRoleMethod = RoleBasedAuthorizationStrategy.class.getDeclaredMethod("assignRole", RoleType.class, Role.class, String.class);
assignRoleMethod.setAccessible(true);
println("HACK! changing visibility of RoleBasedAuthorizationStrategy.assignRole")

/**
 * ===================================
 *         
 *           Permissions
 *
 * ===================================
 */

Set<Permission> adminPermissionSet = new HashSet<Permission>();
adminPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    adminPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> developerPermissionSet = new HashSet<Permission>();
developerPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    developerPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> deployerPermissionSet = new HashSet<Permission>();
deployerPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    deployerPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

Set<Permission> proddeployerPermissionSet = new HashSet<Permission>();
proddeployerPermissions.each { p ->
  def permission = Permission.fromId(p);
  if (permission != null) {
    proddeployerPermissionSet.add(permission);
  } else {
    println("${p} is not a valid permission ID (ignoring)")
  }
}

/**
 * ===================================
 *         
 *      Permissions -> Roles
 *
 * ===================================
 */

// admins
Role adminRole = new Role(globalRoleAdmin, adminPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, adminRole);

//developer
Role developerRole = new Role(globalRoleDev, developerPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, developerRole);

//deployer
Role deployerRole = new Role(globalRoleDeployer, deployerPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, deployerRole);

//Prod deployer
Role proddeployerRole = new Role(globalRolePdeployer, proddeployerPermissionSet);
roleBasedAuthenticationStrategy.addRole(RoleType.Global, proddeployerRole);

/**
 * ===================================
 *         
 *      Roles -> Groups/Users
 *
 * ===================================
 */

access.admins.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, adminRole, l);  
}

access.developers.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, developerRole, l);  
}

access.deployers.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, deployerRole, l);  
}

access.proddeployers.each { l ->
  println("Granting admin to ${l}")
  roleBasedAuthenticationStrategy.assignRole(RoleType.Global, proddeployerRole, l);  
}
Jenkins.instance.save()