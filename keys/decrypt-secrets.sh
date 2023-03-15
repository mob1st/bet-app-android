decrypt() {
  INPUT=$1
  OUTPUT=$2
  gpg --quiet --batch --yes --decrypt --passphrase="$GPG_ENCRYPT_KEY" --output $OUTPUT $INPUT
}

if [[ ! -z "$GPG_ENCRYPT_KEY" ]]; then
  # Decrypt Google Services key
  decrypt keys/google-services.gpg app/google-services.json
else
  echo "GPG_ENCRYPT_KEY is empty"
fi
