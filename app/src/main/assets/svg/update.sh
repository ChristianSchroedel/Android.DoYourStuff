#!/usr/bin/env bash

# Try to find Inkscape or ImageMagick's convert
find_converter()
{
	if [ -z "$INKSCAPE" ]
	then
		INKSCAPE=`which inkscape` ||
			INKSCAPE='/Applications/Inkscape.app/Contents/Resources/bin/inkscape'
	fi

	if [ -x "$INKSCAPE" ]
	then
		converter()
		{
			"$INKSCAPE" \
				"$PWD/$1" \
				-e "$PWD/$2" \
				-w $3 \
				-h $3
		}
	elif which convert &>/dev/null
	then
		converter()
		{
			convert \
				-background none \
				"$1" \
				-thumbnail $3 \
				-strip \
				"$2"
		}
	else
		return 1
	fi
}

# Convert SVG files in multiple resolutions to PNG
#
# @param 1 - output path
update()
{
	[ "$1" ] || return 1

	type converter &>/dev/null || find_converter || {
		echo "error: no Inkscape and no ImageMagick convert" >&2
		return 1
	}

	local SVG SIZE NEGATE
	local DPI MULTIPLIER
	local DIR PNG

	while read SVG SIZE NEGATE
	do
		SIZE=${SIZE:-24}

		while read DPI MULTIPLIER
		do
			DIR="$1-$DPI"

			[ -d "$DIR" ] || mkdir -p "$DIR" || {
				echo "error: couldn't create $DIR" >&2
				return $?
			}

			PNG=${SVG##*/}
			PNG="$DIR/${PNG%.*}.png"

			# skip existing up to date files
			[ -r "$PNG" ] && [ -z "`find \
				"$SVG" \
				-type f \
				-newer "$PNG"`" ] && continue

			converter \
				"$SVG" \
				"$PNG" \
				`echo "$SIZE*$MULTIPLIER" |
					bc -l |
					cut -d '.' -f 1`

			if (( $NEGATE ))
			then
				convert "$PNG" -negate "$PNG"
			fi
		done <<EOF
xxxhdpi 4
xxhdpi 3
xhdpi 2
hdpi 1.5
mdpi 1
ldpi .75
EOF
done
}

update ../../res/mipmap << EOF
ic_launcher.svg 48
EOF

update ../../res/drawable << EOF
ic_action_back.svg 24
ic_action_edit.svg 24
ic_action_new.svg 24
ic_category_car.svg 24
ic_category_default.svg 24
ic_category_food.svg 24
ic_category_gaming.svg 24
ic_category_house.svg 24
ic_category_important.svg 24
ic_category_party.svg 24
ic_category_phone.svg 24
ic_category_school.svg 24
ic_category_shopping.svg 24
ic_category_sport.svg 24
ic_category_template.svg 24
ic_category_work.svg 24
EOF
