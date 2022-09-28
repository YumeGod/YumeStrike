#
# convert a ccache file into KrbCred ASN.1 format
#
import sun.security.krb5.*;
import sun.security.krb5.internal.ccache.*;
import sun.security.krb5.internal.crypto.*;
import sun.security.krb5.internal.*;
import java.io.*;

sub convert {
	local('$cache $cred $sessionKey $princ $realm $tgService $tgsRealm $credInfo $timeStamp $credInfos $encPart $encEncPart $tickets $credMessg');
	$cache = [FileCredentialsCache acquireInstance: $null, $1];
	$cred  = [$cache getCredsList][0];
	$cred  = [$cred setKrbCreds];

	$sessionKey = [$cred getSessionKey];
	$princ      = [$cred getClient];
	$realm      = [$princ getRealm];
	$tgService  = [$cred getServer];
	$tgsRealm   = [$tgService getRealm];

	$credInfo   = [new KrbCredInfo: $sessionKey, $realm, $princ, [$cred flags], [$cred authTime], [$cred startTime], [$cred endTime], [$cred renewTill], $tgsRealm, $tgService, [$cred cAddr]];
	$timeStamp  = [new KerberosTime: [KerberosTime NOW]];
	$credInfos  = cast(@($credInfo), ^KrbCredInfo);
	$encPart    = [new EncKrbCredPart: $credInfos, $timeStamp, $null, $null, $null, $null];
	$encEncPart = [new EncryptedData: [EncryptionKey NULL_KEY], [$encPart asn1Encode], [KeyUsage KU_ENC_KRB_CRED_PART]];
	$tickets    = cast(@([$cred ticket]), ^Ticket);
	$credMessg  = [new KRBCred: $tickets, $encEncPart];

	return [$credMessg asn1Encode];
}
