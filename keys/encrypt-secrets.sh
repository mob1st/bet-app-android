
encrypt() {
  INPUT=$1
  OUTPUT=$2
  gpg --batch --yes --passphrase="$GPG_ENCRYPT_KEY" --cipher-algo AES256 --symmetric --output $OUTPUT $INPUT
}

if [[ ! -z "$GPG_ENCRYPT_KEY" ]]; then
  # Encrypt Google Services key
  encrypt app/google-services.json keys/google-services.gpg
else
  echo "GPG_ENCRYPT_KEY is empty"
fi

chmod +x app/google-services.json
